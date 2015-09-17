package com.utilsframework.android.navigation;

/**
 * Created by CM on 9/18/2015.
 */
abstract class PageLoadingFinishedCallback<T, RequestManager> extends WeakFragmentViewOnPageLoadingFinished<
        NavigationListFragment<T, RequestManager>, T>{
    public PageLoadingFinishedCallback(NavigationListFragment<T, RequestManager> fragment) {
        super(fragment);
    }
}
