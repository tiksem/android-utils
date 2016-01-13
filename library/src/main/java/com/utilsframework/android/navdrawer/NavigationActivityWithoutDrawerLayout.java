package com.utilsframework.android.navdrawer;

import com.utilsframework.android.R;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class NavigationActivityWithoutDrawerLayout extends NavigationDrawerActivity {
    @Override
    protected int getCurrentSelectedNavigationItemId() {
        return 0;
    }

    @Override
    public NavigationMode getNavigationMode() {
        return NavigationMode.NEVER_SHOW_NAVIGATION_TOGGLE;
    }

    @Override
    protected DrawerLayoutAdapter createDrawerLayoutAdapter() {
        return new NoDrawerAdapter();
    }

    @Override
    protected NavigationDrawerMenuAdapter createNavigationDrawerMenuAdapter(int navigationViewId) {
        return new NoMenuAdapter();
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.no_drawer_navigation_activity;
    }
}
