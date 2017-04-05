package com.utilsframework.android.navdrawer;

import android.app.Activity;
import android.view.View;

import com.utilsframework.android.view.GuiUtilities;

/**
 * Created by stykhonenko on 04.11.15.
 */
public class TabsAdapterSwitcher implements TabsAdapter {
    private TabsAdapter tabsAdapter;

    public TabsAdapterSwitcher(TabsAdapter tabsAdapter) {
        this.tabsAdapter = tabsAdapter;
    }

    public void setTabsAdapter(TabsAdapter tabsAdapter) {
        this.tabsAdapter = tabsAdapter;
    }

    public TabsAdapter getTabsAdapter() {
        return tabsAdapter;
    }

    @Override
    public void setOnTabSelected(OnTabSelected listener) {
        tabsAdapter.setOnTabSelected(listener);
    }

    @Override
    public Tab newTab(boolean isSelected) {
        return tabsAdapter.newTab(isSelected);
    }

    @Override
    public void selectTab(int index) {
        tabsAdapter.selectTab(index);
    }
}
