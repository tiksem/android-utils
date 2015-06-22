package com.utilsframework.android.navdrawer;

import android.app.Fragment;

/**
 * Created by CM on 2/20/2015.
 */
public class NavigationDrawerFragment extends Fragment {
    public NavigationDrawerActivity getNavigationActivity() {
        return (NavigationDrawerActivity) getActivity();
    }

    public void replaceFragment(Fragment newFragment, int navigationLevel) {
        getNavigationActivity().replaceFragment(newFragment, navigationLevel);
    }

    public void updateActionBarTitle() {
        getNavigationActivity().updateActionBarTitle();
    }
}
