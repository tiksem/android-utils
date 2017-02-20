package com.utilsframework.android.navdrawer;

import android.support.v4.app.Fragment;

/**
 * Created by CM on 7/22/2015.
 */
public interface NavigationActivityInterface {
    void replaceFragment(Fragment newFragment, int navigationLevel);
    void replaceFragment(int navigationLevel);
    Fragment getLatestBackStackFragment();
    Fragment getCurrentFragment();
    void replaceCurrentFragmentWithoutAddingToBackStack(Fragment newFragment);
}
