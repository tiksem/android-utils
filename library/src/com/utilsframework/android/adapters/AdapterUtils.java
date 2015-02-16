package com.utilsframework.android.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.AbsListView;
import com.utils.framework.collections.NavigationList;
import com.utils.framework.collections.OnAllDataLoaded;
import com.utilsframework.android.view.GuiUtilities;

/**
 * Created by CM on 2/16/2015.
 */
public class AdapterUtils {
    private static View getViewById(ListViewNavigationParams params, int id) {
        if(params.activity != null){
            return params.activity.findViewById(id);
        } else {
            return params.rootView.findViewById(id);
        }
    }

    public static <T> void initListViewNavigation(ListViewNavigationParams<T> params) {
        Activity activity = params.activity;
        final AbsListView listView = (AbsListView) getViewById(params, params.listViewId);
        final View loadingView = getViewById(params, params.loadingViewId);

        final ViewArrayAdapter<T, ?> adapter = params.viewArrayAdapter;
        final NavigationList<T> navigationList = params.navigationList;
        adapter.setElements(navigationList);

        listView.setVisibility(View.INVISIBLE);
        loadingView.setVisibility(View.VISIBLE);

        navigationList.setOnPageLoadingFinished(new NavigationList.OnPageLoadingFinished() {
            @Override
            public void onLoadingFinished() {
                if (navigationList.getLoadedPagesCount() <= 1) {
                    GuiUtilities.swapVisibilities(listView, loadingView);
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

        // load first page
        navigationList.get(0);

        listView.setAdapter(adapter);
    }
}