package com.utilsframework.android.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.utils.framework.OnError;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.R;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.menu.SearchListener;
import com.utilsframework.android.menu.SearchMenuAction;
import com.utilsframework.android.view.GuiUtilities;
import com.utilsframework.android.view.OneVisibleViewInGroupToggle;
import com.utilsframework.android.view.Toasts;
import com.utilsframework.android.view.listview.SwipeLayoutListViewTouchListener;

import java.util.List;

/**
 * Created by CM on 6/21/2015.
 */
public abstract class NavigationListFragment<T, RequestManager> extends Fragment {
    private RequestManager requestManager;
    private ViewArrayAdapter<T, ?> adapter;
    private AbsListView listView;
    private NavigationList<T> elements;
    private Parcelable listViewState;
    private String lastFilter;
    private View loadingView;
    private View noConnectionView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OneVisibleViewInGroupToggle viewsVisibilityToggle;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        requestManager = obtainRequestManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(getRootLayout(), null);
        SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(getActivity());
        swipeRefreshLayout.addView(view);
        return swipeRefreshLayout;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews(view);
        setupListViewListenersAndAdapter();
        setupSwipeLayout(view);
        setupRetryLoadingButton();
    }

    private void setupListViewListenersAndAdapter() {
        adapter = createAdapter(requestManager);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T item = adapter.getElementOfView(view);
                if (item != null) {
                    onListItemClicked(item);
                }
            }
        });
    }

    private void setupViews(View view) {
        listView = (AbsListView) view.findViewById(getListResourceId());
        loadingView = view.findViewById(getLoadingResourceId());
        noConnectionView = view.findViewById(getNoInternetConnectionViewId());

        viewsVisibilityToggle = new OneVisibleViewInGroupToggle(loadingView, listView, noConnectionView);
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

    private void onSwipeRefresh() {
        elements = getNavigationList(requestManager, lastFilter);
        elements.setOnPageLoadingFinished(new NavigationList.OnPageLoadingFinished<T>() {
            @Override
            public void onLoadingFinished(List<T> pageItems) {
                updateAdapterAndViewsState();
                swipeRefreshLayout.setRefreshing(false);
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
    }

    protected abstract RequestManager obtainRequestManager();

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public ViewArrayAdapter<T, ?> getAdapter() {
        return adapter;
    }

    protected abstract int getListResourceId();

    protected abstract int getLoadingResourceId();

    protected abstract ViewArrayAdapter<T, ? extends Object> createAdapter(RequestManager requestManager);
    protected abstract NavigationList<T> getNavigationList(RequestManager requestManager, String filter);

    protected abstract void onListItemClicked(T item);

    protected abstract int getRootLayout();

    protected abstract int getNoInternetConnectionViewId();

    protected int getRetryLoadingButtonId() {
        return 0;
    }

    public void updateNavigationList(String filter) {
        lastFilter = filter;
        elements = getNavigationList(requestManager, filter);
        listViewState = null;
        updateAdapterAndViewsState();
    }

    public NavigationList<T> getElements() {
        return elements;
    }

    public void updateNavigationListWithLastFilter() {
        updateNavigationList(lastFilter);
    }

    private void showView(View view) {
        viewsVisibilityToggle.makeVisible(view);
        swipeRefreshLayout.setEnabled(view == listView);
    }

    private void updateAdapterAndViewsState() {
        adapter.setElements(elements);

        elements.setOnPageLoadingFinished(new NavigationList.OnPageLoadingFinished<T>() {
            @Override
            public void onLoadingFinished(List<T> page) {
                if (!elements.isEmpty() || elements.isAllDataLoaded()) {
                    showView(listView);
                }

                adapter.notifyDataSetChanged();
            }
        });

        elements.setOnError(new OnError() {
            @Override
            public void onError(Throwable e) {
                handleNavigationListError(e);
            }
        });

        if (!elements.isAllDataLoaded() && elements.getElementsCount() <= 0) {
            // load first page
            elements.get(0);
            showView(loadingView);
        } else {
            showView(listView);
        }

        if (listViewState != null) {
            listView.onRestoreInstanceState(listViewState);
        }
    }

    protected void handleNavigationListError(Throwable e) {
        if (elements.getElementsCount() == 0) {
            showView(noConnectionView);
        } else {
            Toasts.error(listView.getContext(), R.string.no_internet_connection);
        }
    }

    protected boolean hasSearchMenu() {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(hasSearchMenu()){
            SearchMenuAction search = new SearchMenuAction(inflater, menu);
            search.setSearchListener(new SearchListener() {
                @Override
                public void onSearch(String filter) {
                    updateNavigationList(filter);
                }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);

        Fragments.executeWhenViewCreated(this, new GuiUtilities.OnViewCreated() {
            @Override
            public void onViewCreated(View view) {
                if (adapter.getElements() == null) {
                    if (elements == null) {
                        elements = getNavigationList(requestManager, null);
                    }

                    updateAdapterAndViewsState();
                }
            }
        });
    }

    public String getLastFilter() {
        return lastFilter;
    }

    public AbsListView getListView() {
        return listView;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public View getNoConnectionView() {
        return noConnectionView;
    }
}
