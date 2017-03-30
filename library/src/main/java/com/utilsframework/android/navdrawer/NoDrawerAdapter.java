package com.utilsframework.android.navdrawer;

import android.view.View;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class NoDrawerAdapter implements DrawerLayoutAdapter {
    @Override
    public void openDrawer(View drawerView) {

    }

    @Override
    public void closeDrawer(View drawerView) {

    }

    @Override
    public boolean isDrawerOpened(View drawerView) {
        return false;
    }

    @Override
    public void setListener(Listener listener) {

    }

    @Override
    public boolean isDrawerVisible(View drawerView) {
        return false;
    }
}
