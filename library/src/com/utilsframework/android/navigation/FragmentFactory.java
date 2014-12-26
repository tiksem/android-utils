package com.utilsframework.android.navigation;

import android.app.ActionBar;
import android.app.Fragment;

/**
 * Created by CM on 12/26/2014.
 */
public interface FragmentFactory {
    public Fragment createFragmentBySelectedItem(int selectedItemId, int tabIndex);
    public void initTab(int currentSelectedItem, int tabIndex, ActionBar.Tab tab);
    public int getTabsCount(int selectedItemId);
}
