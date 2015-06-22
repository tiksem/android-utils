package com.utilsframework.android.adapters.navigation;

import android.app.Activity;
import android.view.View;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by CM on 2/16/2015.
 */
public class ListViewNavigationParams<T> {
    public Activity activity;
    public View rootView;
    public NavigationList<T> navigationList;
    public ViewArrayAdapter<T, ?> adapter;
    public int loadingViewId;
    public int listViewId;
    public int noInternetConnectionViewId;
}
