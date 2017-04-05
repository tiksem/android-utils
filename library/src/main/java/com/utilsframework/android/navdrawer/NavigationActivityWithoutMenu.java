package com.utilsframework.android.navdrawer;

import com.utilsframework.android.R;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class NavigationActivityWithoutMenu extends NavigationActivity {
    @Override
    protected int getCurrentSelectedNavigationItemId() {
        return 0;
    }

    @Override
    public NavigationMode getNavigationMode() {
        return NavigationMode.NEVER_SHOW_NAVIGATION_TOGGLE;
    }

    @Override
    protected MenuLayoutAdapter createMenuLayoutAdapter() {
        return new NoMenuAdapter();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.no_drawer_navigation_activity;
    }
}
