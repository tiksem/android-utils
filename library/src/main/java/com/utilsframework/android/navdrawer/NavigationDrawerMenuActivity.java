package com.utilsframework.android.navdrawer;

import android.support.design.widget.NavigationView;
import android.view.View;

import com.utilsframework.android.R;

/**
 * Created by stykhonenko on 15.10.15.
 */
public abstract class NavigationDrawerMenuActivity extends NavigationActivity {
    private NavigationViewMenuAdapter menuAdapter;

    protected abstract int getMenuId();

    @Override
    protected MenuLayoutAdapter createMenuLayoutAdapter() {
        menuAdapter = new NavigationViewMenuAdapter(this, R.id.drawer_layout,
                R.id.navigation, getMenuId());
        return menuAdapter;
    }

    public NavigationView getNavigationView() {
        return menuAdapter.getMenuView();
    }

    public void registerHeaderItemAsSelectable(int headerItemId) {
        menuAdapter.registerHeaderItemAsSelectable(headerItemId);
    }

    public void registerItemAsSelectable(View item) {
        menuAdapter.registerItemAsSelectable(item);
    }

    public void registerItemAsSelectable(int itemId) {
        View view = findViewById(itemId);
        menuAdapter.registerItemAsSelectable(view);
    }
}
