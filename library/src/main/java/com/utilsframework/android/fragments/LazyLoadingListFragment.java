package com.utilsframework.android.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.utils.framework.collections.LazyLoadingList;
import com.utilsframework.android.R;
import com.utilsframework.android.adapters.AdapterItemClickListenerHolder;
import com.utilsframework.android.adapters.ItemClickListener;
import com.utilsframework.android.adapters.ListAdapter;
import com.utilsframework.android.menu.SearchListener;
import com.utilsframework.android.menu.SearchMenuAction;
import com.utilsframework.android.menu.SortListener;
import com.utilsframework.android.menu.SortMenuAction;
import com.utilsframework.android.network.AsyncRequestExecutionManager;
import com.utilsframework.android.network.CancelStrategy;
import com.utilsframework.android.network.RequestListener;
import com.utilsframework.android.view.OneVisibleViewInGroupToggle;
import com.utilsframework.android.view.Toasts;
import com.utilsframework.android.view.listview.RecyclerViewItemClickListener;
import com.utilsframework.android.view.listview.SwipeLayoutListViewTouchListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by CM on 6/21/2015.
 */
public abstract class LazyLoadingListFragment<T>
        extends RequestManagerFragment implements SortListener {
    private static final String LIST_VIEW_STATE = "LIST_VIEW_STATE";
    private static final String SORT_ORDER = "SORT_ORDER";
    private static final int SHOW_TOAST_MAX_ERROR_COUNT = 1;

    private ListAdapter<T> adapter;
    private View listView;
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
    private AsyncRequestExecutionManager asyncRequestExecutionManager;
    private Set<T> disabledItems = new HashSet<>();

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

        asyncRequestExecutionManager = createAsyncRequestExecutionManager();

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
        if (listView instanceof AbsListView) {
            AbsListView absListView = getAbsListView();
            absListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onListItemClicked(position);
                }
            });
            if (!(adapter instanceof android.widget.ListAdapter)) {
                throw new IllegalStateException("getListResourceId() is AbsListView resource," +
                        "but createAdapter returns " + adapter.getClass().getCanonicalName() + " " +
                        "which doesn't inherit android.widget.ListAdapter. " +
                        "Your adapter is incompatible with AbsListView. Use ViewArrayAdapter as " +
                        "a base class of your adapter for example");
            }

            absListView.setAdapter((android.widget.ListAdapter) adapter);
        } else if(listView instanceof RecyclerView) {
            RecyclerView recyclerView = getRecyclerView();
            recyclerView.setLayoutManager(createRecyclerViewLayoutManager());

            if (!(adapter instanceof RecyclerView.Adapter)) {
                throw new IllegalStateException("getListResourceId() is RecyclerView resource," +
                        "but createAdapter returns " + adapter.getClass().getCanonicalName() + " " +
                        "which doesn't inherit RecyclerView.Adapter. " +
                        "Your adapter is incompatible with RecyclerView. " +
                        "Use RecyclerViewListAdapter as " +
                        "a base class of your adapter for example");
            }

            if (adapter instanceof AdapterItemClickListenerHolder) {
                ((AdapterItemClickListenerHolder)adapter).setItemClickListener(
                        new ItemClickListener() {
                    @Override
                    public void onItemCLicked(int position) {
                        onListItemClicked(position);
                    }
                });
            } else {
                recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(recyclerView,
                        new RecyclerViewItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                onListItemClicked(position);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        }));
            }
            recyclerView.setAdapter((RecyclerView.Adapter) adapter);
        } else {
            throw new IllegalStateException("getListResourceId() returns unsupported " +
                    "listview type. Should be AbsListView or RecyclerView");
        }
    }

    protected RecyclerView.LayoutManager createRecyclerViewLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    private void onListItemClicked(int position) {
        T item = adapter.getElement(position);
        if (item != null && !disabledItems.contains(item)) {
            onListItemClicked(item, position);
        }
    }

    private void setupViews(View view) {
        listView = view.findViewById(getListResourceId());
        if (listView == null) {
            throw new NullPointerException("getListResourceId returns invalid id, " +
                    "listView == null");
        }
        int loadingViewId = getLoadingViewId();
        if (loadingViewId != 0) {
            loadingView = view.findViewById(loadingViewId);
            if (loadingView == null) {
                throw new NullPointerException("getLoadingViewId returns invalid id, " +
                        "loadingView == null");
            }
        }

        noConnectionView = view.findViewById(getNoInternetConnectionViewId());

        int emptyListResourceId = getEmptyListResourceId();
        if (emptyListResourceId != 0) {
            emptyView = view.findViewById(emptyListResourceId);
            if (emptyView == null) {
                throw new NullPointerException("emptyView not found");
            }
        }

        viewsVisibilityToggle = new OneVisibleViewInGroupToggle(loadingView, listView,
                noConnectionView, emptyView);
    }

    private void setupRetryLoadingButton() {
        int buttonId = getRetryLoadingButtonId();
        if (buttonId != 0) {
            View retryButton = noConnectionView.findViewById(buttonId);
            if (retryButton == null) {
                throw new NullPointerException("getRetryLoadingButtonId " +
                        "returns invalid resource. Return 0 if you don't want retry button");
            }

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
        if (listView instanceof AbsListView) {
            listView.setOnTouchListener(new SwipeLayoutListViewTouchListener(swipeRefreshLayout));
        } else if (listView instanceof RecyclerView) {
            getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int topRowVerticalPosition =
                            (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 :
                                    recyclerView.getChildAt(0).getTop();
                    swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);

                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSwipeRefresh();
            }
        });
    }

    private void onRetryLoading() {
        update();
    }

    private void requestGetLazyLoadingList(String filter) {
        elements = getLazyLoadingList(filter);
        sort(elements.getElements());
        onLazyLoadingListChanged(elements);
    }

    protected void onSwipeRefresh() {
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
                handleLoadingError(errorCount, error);
            }
        });

        //load first page
        elements.get(0);
        listViewState = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (listView instanceof AbsListView) {
            listViewState = getAbsListView().onSaveInstanceState();
        }
        restoredSortOrder = getSortOrder();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable listViewState = null;
        if (listView != null && listView instanceof AbsListView) {
            listViewState = getAbsListView().onSaveInstanceState();
        }
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

    public ListAdapter<T> getAdapter() {
        return adapter;
    }

    protected abstract int getListResourceId();

    protected abstract int getLoadingViewId();

    protected abstract ListAdapter<T> createAdapter();
    protected abstract LazyLoadingList<T> getLazyLoadingList(String filter);

    protected abstract void onListItemClicked(T item, int position);

    protected abstract int getRootLayout();

    protected abstract int getNoInternetConnectionViewId();

    protected int getEmptyListResourceId() {
        return 0;
    }

    protected int getRetryLoadingButtonId() {
        return 0;
    }

    public void update(String filter) {
        lastFilter = filter;
        requestGetLazyLoadingList(filter);
        listViewState = null;
        updateAdapterAndViewsState();
    }

    public LazyLoadingList<T> getElements() {
        return elements;
    }

    public void update() {
        update(lastFilter);
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
                handleLoadingError(errorCount, error);
            }
        });

        Object requestForPreloadedData = createRequestForPreloadedData();
        Object requestsForPreloadedData = createRequestsForPreloadedData();

        if (requestForPreloadedData == null && requestsForPreloadedData == null) {
            if (!elements.isAllDataLoaded() && elements.getElementsCount() <= 0) {
                // load first page
                elements.get(0);
                showLoadingOrListView();
            } else {
                showListViewOrEmptyView();
            }
        } else if(requestForPreloadedData != null && requestsForPreloadedData != null) {
            throw new IllegalStateException("createRequestForPreloadedData and createRequestsForPreloadedData " +
                    "returns non null. One of them should return null");
        } else {
            showLoadingOrListView();
            RequestListener listener = new RequestListener<Object, Throwable>() {
                @Override
                public void onSuccess(Object o) {
                    onAllPreloadedDataLoaded(o);
                }

                @Override
                public void onError(Throwable e) {
                    onFirstPageOrPreloadedDataLoadingError(e);
                }
            };

            if (asyncRequestExecutionManager == null) {
                throw new IllegalStateException("implement " +
                        "createAsyncRequestExecutionManager to support " +
                        "createRequestForPreloadedData and createRequestsForPreloadedData");
            }

            if (requestForPreloadedData != null) {
                asyncRequestExecutionManager.execute(requestForPreloadedData,
                        listener, CancelStrategy.INTERRUPT);
            } else {
                asyncRequestExecutionManager.executeMultipleRequests(requestsForPreloadedData,
                        listener, CancelStrategy.INTERRUPT);
            }
        }

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(!elements.isAllDataLoaded());
        }

        if (listViewState != null) {
            if (listView instanceof AbsListView) {
                ((AbsListView)listView).onRestoreInstanceState(listViewState);
            }
        }
    }

    private void showLoadingOrListView() {
        if (loadingView != null) {
            showView(loadingView);
        } else {
            showView(listView);
        }
    }

    protected void onAllPreloadedDataLoaded(Object data) {
        showView(listView);
    }

    protected void onEmptyResults() {
        if (emptyView != null) {
            showView(emptyView);
        } else {
            showView(listView);
        }
    }

    private void showListViewOrEmptyView() {
        if (elements.isAllDataLoaded() && elements.isEmpty()) {
            onEmptyResults();
            return;
        }

        showView(listView);
    }

    protected void handleLoadingError(int errorCount, Throwable e) {
        if (elements.getElementsCount() == 0) {
            onFirstPageOrPreloadedDataLoadingError(e);
        } else if(errorCount <= SHOW_TOAST_MAX_ERROR_COUNT) {
            Toasts.toast(listView.getContext(), getErrorToastMessage(e));
        }
    }

    protected void onFirstPageOrPreloadedDataLoadingError(Throwable e) {
        showLoadingOrListView();
    }

    protected int getErrorToastMessage(Throwable e) {
        return R.string.no_internet_connection;
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
                    update(filter);
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

    public boolean isRefreshing() {
        return swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing();
    }

    protected int getSearchMenuId() {
        return R.menu.search;
    }

    public final String getLastFilter() {
        return lastFilter;
    }

    public final View getListView() {
        return listView;
    }

    public final AbsListView getAbsListView() {
        return (AbsListView) listView;
    }

    public final RecyclerView getRecyclerView() {
        return (RecyclerView) listView;
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
        update();
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
        if (!(listView instanceof AbsListView)) {
            throw new UnsupportedOperationException("getSelectedItems is supported only for listview");
        }

        SparseBooleanArray positions = getAbsListView().getCheckedItemPositions();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (positions.get(i)) {
                result.add(adapter.getElement(i));
            }
        }

        return result;
    }

    protected void onLazyLoadingListChanged(LazyLoadingList<T> lazyLoadingList) {

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

    public void setItemClickEnabled(T item, boolean enabled) {
        if (enabled) {
            disabledItems.remove(item);
        } else {
            disabledItems.add(item);
        }
    }

    // override one of these methods to load some data before loading navigation list
    protected Object createRequestForPreloadedData() {
        return null;
    }
    protected Object createRequestsForPreloadedData() {
        return null;
    }

    // used only to support createRequestForPreloadedData and createRequestsForPreloadedData functionality
    protected AsyncRequestExecutionManager createAsyncRequestExecutionManager() {
        return null;
    }

    public Set<T> getDisabledItems() {
        return disabledItems;
    }
}
