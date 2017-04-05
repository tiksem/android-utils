package com.utilsframework.android.navdrawer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by stykhonenko on 19.10.15.
 */
public interface TabsAdapter {
    interface Tab {
        void setText(CharSequence text);
        void setText(int id);
        void setIcon(int resourceId);
        int getIndex();
        Object getTabHandler();
    }

    interface OnTabSelected {
        void onTabSelected(Tab tab);
    }

    void setOnTabSelected(OnTabSelected listener);
    Tab getTab(int index);
    void selectTab(int index);
    void setupViewPager(ViewPager viewPager);
}
