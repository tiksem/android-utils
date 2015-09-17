package com.utilsframework.android.navigation;

import com.utils.framework.OnError;
import com.utilsframework.android.fragments.WeakFragmentReferenceHolder;

/**
 * Created by CM on 9/18/2015.
 */
class ErrorCallback extends WeakFragmentReferenceHolder<NavigationListFragment> implements OnError {
    public ErrorCallback(NavigationListFragment fragment) {
        super(fragment);
    }

    @Override
    public void onError(Throwable e) {
        if (getView() != null) {
            getFragment().handleNavigationListError(e);
        }
    }
}
