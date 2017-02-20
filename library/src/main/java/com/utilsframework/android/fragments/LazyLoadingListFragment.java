package com.utilsframework.android.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.utils.framework.collections.LazyLoadingList;
import com.utilsframework.android.R;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.menu.SearchListener;
import com.utilsframework.android.menu.SearchMenuAction;
import com.utilsframework.android.menu.SortListener;
import com.utilsframework.android.menu.SortMenuAction;
import com.utilsframework.android.network.retrofit.RetrofitRequestManager;
import com.utilsframework.android.view.OneVisibleViewInGroupToggle;
import com.utilsframework.android.view.Toasts;
import com.utilsframework.android.view.listview.SwipeLayoutListViewTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CM on 6/21/2015.
 */
public abstract class LazyLoadingListFragment<T>
        extends RequestManagerFragment implements SortListener {
    private static final String LIST_VIEW_STATE = "LIST_VIEW_STATE";
    private static final String SORT_ORDER = "SORT_ORDER";
    private static final int SHOW_TOAST_MAX_ERROR_COUNT = 1;

    private ViewArrayAdapter<T, ?> adapter;
    private AbsListView listView;
    private View emptyView;
    private LazyLoadingList<T> elements;
    private Parcelable listViewState;
    private String lastFilter;
    private View loadingView;
    private View noConnectionView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OneVisibleViewInGroupToggle viewsVisibilityToggle;
    private SortMenuAction sortAction;
    private int restoredSortOrder = 0;
    private boolean firstViewCreate = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(getRootLayout(), null);
        if (useSwipeRefresh()) {
            SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(getActivity());
            swipeRefreshLayout.addView(view);
            return swipeRefreshLayout;
        } else {
            return view;
        }
    }

    protected String getInitialFilter() {
        return null;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (firstViewCreate) {
            lastFilter = getInitialFilter();
            firstViewCreate = false;
        }

        setupViews(view);
        setupListViewListenersAndAdapter();
        if (useSwipeRefresh()) {
            setupSwipeLayout(view);
        }
        setupRetryLoadingButton();

        if (elements == null) {
            requestGetLazyLoadingList(lastFilter);
        }

        updateAdapterAndViewsState();
    }

    private void setupListViewListenersAndAdapter() {
        adapter = createAdapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T item = adapter.getElementOfView(view);
                if (item != null) {
                    onListItemClicked(item, position);
                }
            }
        });
    }

    private void setupViews(View view) {
        listView = (AbsListView) view.findViewById(getListResourceId());
        if (listView == null) {
            throw new NullPointerException("getListResourceId returns invalid id, " +
                    "listView == null");
        }
        loadingView = view.findViewById(getLoadingResourceId());
        if (loadingView == null) {
            throw new NullPointerException("getLoadingResourceId returns invalid id, " +
                    "loadingView == null");
        }

        noConnectionView = view.findViewById(getNoInternetConnectionViewId());

        int emptyListResourceId = getEmptyListResourceId();
        if (emptyListResourceId != 0) {
            emptyView = view.findViewById(emptyListResourceId);
            if (emptyView == null) {
                throw new NullPointerException("emptyView not found");
            }
        }

        viewsVisibilityToggle = new OneVisibleViewInGroupToggle(loadingView, listView, noConnectionView, emptyView);
    }

    private void setupRetryLoadingButton() {
        int buttonId = getRetryLoadingButtonId();
        if (buttonId != 0) {
            View retryButton = noConnectionView.findViewById(buttonId);
            retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRetryLoading();
                }
            });
        }
    }

    private void setupSwipeLayout(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view;
        listView.setOnTouchListener(new SwipeLayoutListViewTouchListener(swipeRefreshLayout));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeRefresh();
            }
        });
    }

    private void onRetryLoading() {
        updateNavigationListWithLastFilter();
    }

    private void requestGetLazyLoadingList(String filter) {
        elements = getLazyLoadingList(getRequestManager(), filter);
        sort(elements.getElements());
        onNavigationListChanged(elements);
    }

    private void onSwipeRefresh() {
        requestGetLazyLoadingList(lastFilter);
        elements.setOnPageLoadingFinished(new LazyLoadingList.OnPageLoadingFinished<T>() {
            @Override
            public void onLoadingFinished(List<T> elements) {
                updateAdapterAndViewsState();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        elements.setOnError(new LazyLoadingList.PageLoadingError() {
            @Override
            public void onError(int errorCount, Throwable error) {
                handleNavigationListError(errorCount, error);
            }
        });

        //load first page
        elements.get(0);
        listViewState = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listViewState = listView.onSaveInstanceState();
        restoredSortOrder = getSortOrder();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable listViewState = listView.onSaveInstanceState();
        if (listViewState != null) {
            outState.putParcelable(LIST_VIEW_STATE, listViewState);
        }

        outState.putInt(SORT_ORDER, getSortOrder());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            listViewState = savedInstanceState.getParcelable(LIST_VIEW_STATE);
            restoredSortOrder = savedInstanceState.getInt(SORT_ORDER, 0);
        }
    }

    public ViewArrayAdapter<T, ?> getAdapter() {
        return adapter;
    }

    protected abstract int getListResourceId();

    protected abstract int getLoadingResourceId();

    protected abstract ViewArrayAdapter<T, ? extends Object> createAdapter();
    protected abstract LazyLoadingList<T> getLazyLoadingList(RetrofitRequestManager requestManager,
                                                             String filter);

    protected abstract void onListItemClicked(T item, int position);

    protected abstract int getRootLayout();

    protected abstract int getNoInternetConnectionViewId();

    protected int getEmptyListResourceId() {
        return 0;
    }

    protected int getRetryLoadingButtonId() {
        return 0;
    }

    public void updateNavigationList(String filter) {
        lastFilter = filter;
        requestGetLazyLoadingList(filter);
        listViewState = null;
        updateAdapterAndViewsState();
    }

    public LazyLoadingList<T> getElements() {
        return elements;
    }

    public void updateNavigationListWithLastFilter() {
        updateNavigationList(lastFilter);
    }

    private void showView(View view) {
        if (view == null) {
            throw new NullPointerException();
        }

        viewsVisibilityToggle.makeVisible(view);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(view == listView || view == emptyView);
        }

        if (view == listView) {
            onListViewIsShown();
        } else if(view == emptyView) {
            onEmptyViewIsShown();
        }
    }

    protected void onEmptyViewIsShown() {

    }

    protected void onListViewIsShown() {

    }

    private void updateAdapterAndViewsState() {
        adapter.setElements(elements);

        elements.setOnPageLoadingFinished(new LazyLoadingList.OnPageLoadingFinished<T>() {
            @Override
            public void onLoadingFinished(List<T> page) {
                if (elements.getElementsCount() > 0 || elements.isAllDataLoaded()) {
                    showListViewOrEmptyView();
                }

                if (!page.isEmpty()) {
                    adapter.notifyDataSetChanged();
                }
            }
        });

        elements.setOnError(new LazyLoadingList.PageLoadingError() {
            @Override
            public void onError(int errorCount, Throwable error) {
                handleNavigationListError(errorCount, error);
            }
        });

        if (!elements.isAllDataLoaded() && elements.getElementsCount() <= 0) {
            // load first page
            elements.get(0);
            showView(loadingView);
        } else {
            showListViewOrEmptyView();
        }

        if (listViewState != null) {
            listView.onRestoreInstanceState(listViewState);
        }
    }

    protected void onEmptyResults() {
        showView(emptyView);
    }

    private void showListViewOrEmptyView() {
        if (emptyView != null && elements.isAllDataLoaded() && elements.isEmpty()) {
            onEmptyResults();
            return;
        }

        showView(listView);
    }

    protected void handleNavigationListError(int errorCount, Throwable e) {
        if (elements.getElementsCount() == 0) {
            showView(noConnectionView);
        } else if(errorCount <= SHOW_TOAST_MAX_ERROR_COUNT) {
            Toasts.toast(listView.getContext(), R.string.no_internet_connection);
        }
    }

    protected boolean hasSearchMenu() {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        if(hasSearchMenu()){
            SearchMenuAction search = new SearchMenuAction(inflater, menu) {
                @Override
                protected void onExpandCollapse(boolean expanded) {
                    onSearchViewExpandCollapse(menu, expanded);
                }

                @Override
                protected int getSearchMenuId() {
                    return LazyLoadingListFragment.this.getSearchMenuId();
                }
            };
            search.setSearchListener(new SearchListener() {
                @Override
                public void onSearch(String filter) {
                    updateNavigationList(filter);
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);

        int sortMenuId = getSortMenuId();
        if (sortMenuId != 0) {
            inflater.inflate(sortMenuId, menu);
            int sortOrder = restoredSortOrder;
            if (sortOrder == 0) {
                sortOrder = getInitialSortOrder();
            }

            sortAction = new SortMenuAction(menu, getSortMenuGroupId(), sortOrder);
            sortAction.setSortListener(this);
        }
    }

    protected int getSearchMenuId() {
        return R.menu.search;
    }

    public final String getLastFilter() {
        return lastFilter;
    }

    public final AbsListView getListView() {
        return listView;
    }

    public final View getLoadingView() {
        return loadingView;
    }

    public final View getNoConnectionView() {
        return noConnectionView;
    }

    public final View getEmptyView() {
        return emptyView;
    }

    @Override
    public void onSortOrderChanged(int newSortOrder) {
        updateNavigationListWithLastFilter();
    }

    protected int getInitialSortOrder() {
        return 0;
    }

    protected int getSortMenuId() {
        return 0;
    }

    protected int getSortMenuGroupId() {
        return 0;
    }

    protected final int getSortOrder() {
        if (sortAction == null) {
            return 0;
        }

        return sortAction.getSortOrder();
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    protected boolean useSwipeRefresh() {
        return true;
    }

    protected void onSearchViewExpandCollapse(Menu menu, boolean expanded) {

    }

    public List<T> getSelectedItems() {
        List<T> result = new ArrayList<>();
        SparseBooleanArray positions = getListView().getCheckedItemPositions();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (positions.get(i)) {
                result.add(adapter.getElement(i));
            }
        }

        return result;
    }

    protected void onNavigationListChanged(LazyLoadingList<T> lazyLoadingList) {

    }

    private void sort(List<T> items) {
        int sortOrder = getSortOrder();
        if (sortOrder != 0) {
            sort(items, sortOrder);
        }
    }

    protected void sort(List<T> items, int sortingOrder) {
        throw new UnsupportedOperationException();
    }
}