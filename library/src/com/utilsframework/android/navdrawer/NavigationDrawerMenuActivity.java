package com.utilsframework.android.navdrawer;

import android.support.design.widget.NavigationView;

/**
 * Created by stykhonenko on 15.10.15.
 */
public abstract class NavigationDrawerMenuActivity extends NavigationDrawerActivity {
    private NavigationViewMenuAdapter menuAdapter;

    protected abstract int getMenuId();

    @Override
    protected NavigationDrawerMenuAdapter createNavigationDrawerMenuAdapter(int navigationViewId) {
        menuAdapter = new NavigationViewMenuAdapter(this, navigationViewId, getMenuId());
        return menuAdapter;
    }

    public NavigationView getNavigationView() {
        return menuAdapter.getNavigationMenuView();
    }
}
