package com.utilsframework.android.navigation;

import android.app.Fragment;

/**
 * Created by CM on 12/26/2014.
 */
public interface FragmentFactory {
    public Fragment createFragmentBySelectedItem(int selectedItemId);
}
