package com.utilsframework.android.navdrawer;

import android.view.View;

/**
 * Created by stykhonenko on 15.10.15.
 */
public interface NavigationDrawerMenuAdapter {
    interface OnItemSelectedListener {
        void onItemSelected(int id);
    }

    void setOnItemSelectedListener(OnItemSelectedListener listener);
    void applySelectItemVisualStyle(int id);
    View getNavigationMenuView();
}
