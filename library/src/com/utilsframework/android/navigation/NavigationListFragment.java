package com.utilsframework.android.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.IOErrorListener;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.adapters.navigation.ListViewNavigation;
import com.utilsframework.android.adapters.navigation.ListViewNavigationParams;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.menu.SearchListener;
import com.utilsframework.android.menu.SearchMenuAction;
import com.utilsframework.android.network.IOErrorListenersSet;
import com.utilsframework.android.view.GuiUtilities;

import java.io.IOException;

/**
 * Created by CM on 6/21/2015.
 */
public abstract class NavigationListFragment<T, RequestManager extends IOErrorListenersSet> extends Fragment {
    private IOErrorListener ioErrorListener;
    private RequestManager requestManager;
    private ViewArrayAdapter<T, ?> adapter;
    protected AbsListView listView;
    private ListViewNavigation<T> navigation;
    private NavigationList<T> elements;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        requestManager = obtainRequestManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(getRootLayout(), null);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (AbsListView) view.findViewById(getListResourceId());

        adapter = createAdapter(requestManager);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T item = adapter.getElementOfView(view);
                if (item != null) {
                    onListItemClicked(item);
                }
            }
        });

        ioErrorListener = new IOErrorListener() {
            @Override
            public void onIOError(IOException error) {
                listView.setVisibility(View.INVISIBLE);
                view.findViewById(getLoadingResourceId()).setVisibility(View.INVISIBLE);
                view.findViewById(getNoInternetConnectionViewId()).setVisibility(View.VISIBLE);
            }
        };
        requestManager.addIOErrorListener(ioErrorListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requestManager.removeIOErrorListener(ioErrorListener);
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

    public void updateNavigationList(String filter) {
        elements = getNavigationList(requestManager, filter);
        updateNavigation();
    }

    private void updateNavigation() {
        if (navigation != null) {
            navigation.destroy();
        }

        ListViewNavigationParams<T> params = new ListViewNavigationParams<T>();
        params.adapter = adapter;
        params.navigationList = elements;
        params.rootView = getView();
        params.listViewId = getListResourceId();
        params.loadingViewId = getLoadingResourceId();
        params.noInternetConnectionViewId = getNoInternetConnectionViewId();
        navigation = createNavigation(params);
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

                    updateNavigation();
                }
            }
        });
    }

    protected ListViewNavigation<T> createNavigation(ListViewNavigationParams<T> params) {
        return new ListViewNavigation<T>(params) {
            @Override
            protected void handleError(NavigationList<T> navigationList,
                                       View noConnectionView, AbsListView listView, View loadingView, Throwable e) {
                if (!shouldOverrideHandlingErrorBehavior(e)) {
                    super.handleError(navigationList, noConnectionView, listView, loadingView, e);
                }
            }
        };
    }

    protected boolean shouldOverrideHandlingErrorBehavior(Throwable e) {
        return false;
    }
}
