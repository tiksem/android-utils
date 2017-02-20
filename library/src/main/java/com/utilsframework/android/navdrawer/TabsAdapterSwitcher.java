package com.utilsframework.android.navdrawer;

import android.app.Activity;
import android.view.View;

import com.utilsframework.android.view.GuiUtilities;

/**
 * Created by stykhonenko on 04.11.15.
 */
public class TabsAdapterSwitcher implements TabsAdapter {
    private TabsAdapter tabsAdapter;
    private View currentView;

    public TabsAdapterSwitcher(Activity activity, int viewStubId) {
        currentView = activity.findViewById(viewStubId);
    }

    public void setTabsAdapter(TabsAdapter tabsAdapter) {
        this.tabsAdapter = tabsAdapter;
        View view = tabsAdapter.getView();
        GuiUtilities.replaceView(currentView, view);
        currentView = view;
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
    public void removeAllTabs() {
        tabsAdapter.removeAllTabs();
    }

    @Override
    public void selectTab(int index) {
        tabsAdapter.selectTab(index);
    }

    @Override
    public View getView() {
        return tabsAdapter.getView();
    }
}
