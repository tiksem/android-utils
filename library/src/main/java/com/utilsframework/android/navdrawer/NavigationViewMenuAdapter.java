package com.utilsframework.android.navdrawer;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.utilsframework.android.menu.MenuUtils;

import java.util.List;

/**
 * Created by stykhonenko on 15.10.15.
 */
public class NavigationViewMenuAdapter implements NavigationDrawerMenuAdapter {
    private NavigationView navigationView;
    private OnItemSelectedListener onItemSelectedListener;

    public NavigationViewMenuAdapter(Activity activity, int navigationViewId, int menuResourceId) {
        navigationView = (NavigationView) activity.findViewById(navigationViewId);
        navigationView.inflateMenu(menuResourceId);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                onItemSelected(menuItem);
                return true;
            }
        });
    }

    protected void onItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(itemId);
        }
        applySelectItemVisualStyle(itemId);
    }

    public OnItemSelectedListener getOnItemSelectedListener() {
        return onItemSelectedListener;
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        onItemSelectedListener = listener;
    }

    @Override
    public void applySelectItemVisualStyle(int id) {
        List<MenuItem> menuItems = MenuUtils.getAllItemsRecursive(
                getNavigationMenuView().getMenu());
        for (MenuItem menuItem : menuItems) {
            menuItem.setChecked(menuItem.getItemId() == id);
        }
    }

    @Override
    public NavigationView getNavigationMenuView() {
        return navigationView;
    }
}
