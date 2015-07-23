package com.utilsframework.android.adapters.navigation;

import android.app.Activity;
import android.view.View;
import android.widget.AbsListView;
import com.utils.framework.Destroyable;
import com.utils.framework.OnError;
import com.utils.framework.collections.NavigationList;
import com.utils.framework.collections.OnAllDataLoaded;
import com.utilsframework.android.R;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.view.Toasts;

import java.util.List;

/**
 * Created by CM on 6/21/2015.
 */
public class ListViewNavigation<T> implements Destroyable {
    private final ListViewNavigationParams<T> params;

    private static View getViewById(ListViewNavigationParams params, int id) {
        if(params.activity != null){
            return params.activity.findViewById(id);
        } else {
            return params.rootView.findViewById(id);
        }
    }

    public ListViewNavigation(ListViewNavigationParams<T> params) {
        Activity activity = params.activity;
        final AbsListView listView = (AbsListView) getViewById(params, params.listViewId);
        final View loadingView = getViewById(params, params.loadingViewId);
        final View noConnectionView = getViewById(params, params.noInternetConnectionViewId);

        final ViewArrayAdapter<T, ?> adapter = params.adapter;
        final NavigationList<T> navigationList = params.navigationList;
        adapter.setElements(navigationList);

        this.params = params;

        listView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.VISIBLE);

        navigationList.setOnPageLoadingFinished(new NavigationList.OnPageLoadingFinished<T>() {
            @Override
            public void onLoadingFinished(List<T> elements) {
                if (!elements.isEmpty() || navigationList.isAllDataLoaded()) {
                    listView.setVisibility(View.VISIBLE);
                    loadingView.setVisibility(View.INVISIBLE);
                }

                adapter.notifyDataSetChanged();
            }
        });

        navigationList.setOnAllDataLoaded(new OnAllDataLoaded() {
            @Override
            public void onAllDataLoaded() {
                adapter.notifyDataSetChanged();
            }
        });

        navigationList.setOnError(new OnError() {
            @Override
            public void onError(Throwable e) {
                handleError(navigationList, noConnectionView, listView, loadingView, e);
            }
        });

        if (!navigationList.isAllDataLoaded() && navigationList.getElementsCount() <= 0) {
            // load first page
            navigationList.get(0);
        } else {
            loadingView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }

        listView.setAdapter(adapter);
        if (params.listViewState != null) {
            listView.onRestoreInstanceState(params.listViewState);
        }
    }

    protected void handleError(NavigationList<T> navigationList,
                               View noConnectionView,
                               AbsListView listView,
                               View loadingView,
                               Throwable e) {
        if (navigationList.getElementsCount() == 0) {
            listView.setVisibility(View.INVISIBLE);
            loadingView.setVisibility(View.INVISIBLE);
            noConnectionView.setVisibility(View.VISIBLE);
        } else {
            Toasts.error(listView.getContext(), R.string.no_internet_connection);
        }
    }

    @Override
    public void destroy() {
        params.navigationList.setOnPageLoadingFinished(null);
        params.navigationList.setOnAllDataLoaded(null);
        params.navigationList.setOnError(null);
        params.adapter.setElements(null);
    }
}
