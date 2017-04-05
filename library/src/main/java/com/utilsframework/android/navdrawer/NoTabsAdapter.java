package com.utilsframework.android.navdrawer;

import android.support.v4.view.ViewPager;
import android.view.View;

public class NoTabsAdapter implements TabsAdapter {
    @Override
    public void setOnTabSelected(OnTabSelected listener) {

    }

    @Override
    public Tab getTab(int index) {
        return null;
    }

    @Override
    public void selectTab(int index) {

    }

    @Override
    public void setupViewPager(ViewPager viewPager) {

    }
}
