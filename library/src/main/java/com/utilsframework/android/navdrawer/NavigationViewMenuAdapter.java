package com.utilsframework.android.navdrawer;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;

import com.utilsframework.android.menu.MenuUtils;

import java.util.List;

/**
 * Created by stykhonenko on 15.10.15.
 */
public class NavigationViewMenuAdapter extends AndroidSupportDrawerLayoutMenuAdapter {
    private NavigationView navigationView;

    public NavigationViewMenuAdapter(Activity activity, int drawerLayoutId,
                                     int navigationViewId,
                                     int menuResourceId) {
        super(activity, drawerLayoutId);
        navigationView = (NavigationView) activity.findViewById(navigationViewId);
        navigationView.inflateMenu(menuResourceId);
        setMenuView(navigationView);
    }

    @Override
    protected void setListenerToMenuView(final Listener listener, View menuView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        listener.onItemSelected(itemId);
                        applySelectItemVisualStyle(itemId);
                        return true;
                    }
                });
    }

    @Override
    public void applySelectItemVisualStyle(int id) {
        List<MenuItem> menuItems = MenuUtils.getAllItemsRecursive(navigationView.getMenu());
        for (MenuItem menuItem : menuItems) {
            menuItem.setChecked(menuItem.getItemId() == id);
        }
    }

    public void registerHeaderItemAsSelectable(int headerItemId) {
        View item = navigationView.getHeaderView(0).findViewById(headerItemId);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListener().onItemSelected(v.getId());
            }
        });
    }

    public NavigationView getMenuView() {
        return navigationView;
    }
}
