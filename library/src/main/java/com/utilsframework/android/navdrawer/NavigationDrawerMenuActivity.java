package com.utilsframework.android.navdrawer;

import android.support.design.widget.NavigationView;
import android.view.View;

/**
 * Created by stykhonenko on 15.10.15.
 */
public abstract class NavigationDrawerMenuActivity extends NavigationActivity {
    private NavigationViewMenuAdapter menuAdapter;

    protected abstract int getMenuId();

    @Override
    protected final NavigationDrawerMenuAdapter createNavigationDrawerMenuAdapter(
            int navigationViewId) {
        menuAdapter = createNavigationViewMenuAdapter(navigationViewId);
        return menuAdapter;
    }

    protected NavigationViewMenuAdapter createNavigationViewMenuAdapter(int navigationViewId) {
        return new NavigationViewMenuAdapter(this, navigationViewId, getMenuId());
    }

    public NavigationView getNavigationView() {
        return menuAdapter.getNavigationMenuView();
    }

    public void registerHeaderItemAsSelectable(int headerItemId) {
        menuAdapter.registerHeaderItemAsSelectable(headerItemId);
    }

    public void registerItemAsSelectable(View item) {
        menuAdapter.registerItemAsSelectable(item);
    }
}
