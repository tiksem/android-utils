package com.utilsframework.android.navdrawer;

import android.view.View;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class NoMenuAdapter implements MenuLayoutAdapter {
    @Override
    public void applySelectItemVisualStyle(int id) {

    }

    @Override
    public View getMenuView() {
        return null;
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setListener(Listener listener) {

    }
}
