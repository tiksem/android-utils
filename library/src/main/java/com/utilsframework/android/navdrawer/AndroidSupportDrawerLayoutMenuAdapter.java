package com.utilsframework.android.navdrawer;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class AndroidSupportDrawerLayoutMenuAdapter extends BaseMenuLayoutAdapter {
    private View menuView;
    private DrawerLayout drawerLayout;

    public AndroidSupportDrawerLayoutMenuAdapter(Activity activity, int drawerLayoutId) {
        drawerLayout = (DrawerLayout) activity.findViewById(drawerLayoutId);
    }

    @Override
    public void open() {
        drawerLayout.openDrawer(menuView);
    }

    @Override
    public void close() {
        drawerLayout.closeDrawer(menuView);
    }

    @Override
    public boolean isOpen() {
        return drawerLayout.isDrawerOpen(menuView);
    }

    @Override
    public boolean isVisible() {
        return drawerLayout.isDrawerVisible(menuView);
    }

    @Override
    public void setListener(final Listener listener) {
        super.setListener(listener);
        if (listener == null) {
            drawerLayout.setDrawerListener(null);
        } else {
            drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View view, float v) {

                }

                @Override
                public void onDrawerOpened(View view) {
                    listener.onOpened();
                }

                @Override
                public void onDrawerClosed(View view) {
                    listener.onClosed();
                }

                @Override
                public void onDrawerStateChanged(int i) {

                }
            });
            setListenerToMenuView(listener, menuView);
        }
    }

    public View getMenuView() {
        return menuView;
    }

    protected final void setMenuView(View menuView) {
        this.menuView = menuView;
    }

    protected abstract void setListenerToMenuView(Listener listener, View menuView);
}
