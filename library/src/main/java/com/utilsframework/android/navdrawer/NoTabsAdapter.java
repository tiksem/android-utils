package com.utilsframework.android.navdrawer;

import android.view.View;

public class NoTabsAdapter implements TabsAdapter {
    @Override
    public void setOnTabSelected(OnTabSelected listener) {

    }

    @Override
    public Tab newTab(boolean isSelected) {
        return null;
    }

    @Override
    public void removeAllTabs() {

    }

    @Override
    public void selectTab(int index) {

    }

    @Override
    public View getView() {
        return null;
    }
}
