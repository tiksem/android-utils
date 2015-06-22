package com.utilsframework.android.navdrawer;

import android.app.ActionBar;
import android.app.Fragment;

/**
 * Created by CM on 12/26/2014.
 */
public interface FragmentFactory {
    public Fragment createFragmentBySelectedItem(int selectedItemId, int tabIndex, int navigationLevel);
    public void initTab(int currentSelectedItem, int tabIndex, int navigationLevel, ActionBar.Tab tab);
    public int getTabsCount(int selectedItemId, int navigationLevel);
}
