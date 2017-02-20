package com.utilsframework.android.navdrawer;

import android.support.v4.app.Fragment;

/**
 * Created by CM on 12/26/2014.
 */
public interface FragmentFactory {
    Fragment createFragmentBySelectedItem(int selectedItemId, int tabIndex, int navigationLevel);
    void initTab(int currentSelectedItem, int tabIndex, int navigationLevel, TabsAdapter.Tab tab);
    int getTabsCount(int selectedItemId, int navigationLevel);
}
