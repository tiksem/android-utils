package com.utilsframework.android.navdrawer;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;

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
                menuItem.setChecked(true);
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(menuItem.getItemId());
                }
                return true;
            }
        });
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        onItemSelectedListener = listener;
    }

    @Override
    public void selectItem(int id) {
        navigationView.getMenu().findItem(id).setChecked(true);
    }

    @Override
    public NavigationView getNavigationMenuView() {
        return navigationView;
    }
}
