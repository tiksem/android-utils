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
public class NavigationViewMenuAdapter implements NavigationDrawerMenuAdapter {
    private NavigationView navigationView;
    private OnItemSelectedListener onItemSelectedListener;

    public NavigationViewMenuAdapter(Activity activity, int navigationViewId, int menuResourceId) {
        navigationView = (NavigationView) activity.findViewById(navigationViewId);
        navigationView.inflateMenu(menuResourceId);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        onItemSelected(itemId);
                        return true;
                    }
                });
    }

    protected void onItemSelected(int itemId) {
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

    public void registerItemAsSelectable(View item) {
        final int id = item.getId();
        if (id == View.NO_ID) {
            throw new IllegalArgumentException("Item without id cannot be selectable");
        }

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelected(v.getId());
            }
        });
    }

    public void registerHeaderItemAsSelectable(int headerItemId) {
        View item = getNavigationMenuView().getHeaderView(0).findViewById(headerItemId);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelected(v.getId());
            }
        });
    }
}
