package com.utilsframework.android.navdrawer;

import android.view.View;

/**
 * Created by stykhonenko on 19.10.15.
 */
public interface MenuLayoutAdapter {
    interface Listener {
        void onOpened();
        void onClosed();
        void onItemSelected(int id);
    }

    void applySelectItemVisualStyle(int id);
    View getMenuView();

    void open();
    void close();
    boolean isOpen();
    boolean isVisible();
    void setListener(Listener listener);
}
