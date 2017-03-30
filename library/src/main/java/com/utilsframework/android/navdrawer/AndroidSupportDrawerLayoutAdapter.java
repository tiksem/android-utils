package com.utilsframework.android.navdrawer;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class AndroidSupportDrawerLayoutAdapter implements DrawerLayoutAdapter {
    private DrawerLayout drawerLayout;

    public AndroidSupportDrawerLayoutAdapter(Activity activity, int drawerLayoutId) {
        drawerLayout = (DrawerLayout) activity.findViewById(drawerLayoutId);
    }

    @Override
    public void openDrawer(View drawerView) {
        drawerLayout.openDrawer(drawerView);
    }

    @Override
    public void closeDrawer(View drawerView) {
        drawerLayout.closeDrawer(drawerView);
    }

    @Override
    public boolean isDrawerOpened(View drawerView) {
        return drawerLayout.isDrawerOpen(drawerView);
    }

    @Override
    public boolean isDrawerVisible(View drawerView) {
        return drawerLayout.isDrawerVisible(drawerView);
    }

    @Override
    public void setListener(final Listener listener) {
        if (listener == null) {
            drawerLayout.setDrawerListener(null);
        } else {
            drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View view, float v) {

                }

                @Override
                public void onDrawerOpened(View view) {
                    listener.onDrawerOpened(view);
                }

                @Override
                public void onDrawerClosed(View view) {
                    listener.onDrawerClosed(view);
                }

                @Override
                public void onDrawerStateChanged(int i) {

                }
            });
        }
    }
}
