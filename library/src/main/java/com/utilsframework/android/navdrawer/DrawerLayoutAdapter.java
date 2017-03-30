package com.utilsframework.android.navdrawer;

import android.view.View;

/**
 * Created by stykhonenko on 19.10.15.
 */
public interface DrawerLayoutAdapter {
    interface Listener {
        void onDrawerOpened(View drawerView);
        void onDrawerClosed(View drawerView);
    }

    void openDrawer(View drawerView);
    void closeDrawer(View drawerView);
    boolean isDrawerOpened(View drawerView);
    boolean isDrawerVisible(View drawerView);
    void setListener(Listener listener);
}
