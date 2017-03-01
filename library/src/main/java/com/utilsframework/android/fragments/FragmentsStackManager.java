package com.utilsframework.android.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.utils.framework.collections.LinkedStack;
import com.utils.framework.collections.Stack;
import com.utilsframework.android.navdrawer.FragmentsNavigationInterface;

public class FragmentsStackManager implements FragmentsNavigationInterface {
    private int navigationLevel;
    private FragmentActivity activity;
    private int contentId;
    private Stack<Fragment> backStack = new LinkedStack<>();

    public FragmentsStackManager(FragmentActivity activity, int contentId) {
        this.activity = activity;
        this.contentId = contentId;
    }

    @Override
    public void replaceFragment(Fragment newFragment, final int navigationLevel) {
        final int lastNavigationLevel = this.navigationLevel;

        final int backStackEntryCount = activity.getSupportFragmentManager().getBackStackEntryCount();
        Fragments.replaceOrAddFragmentAndAddToBackStack(activity, contentId, newFragment);

        FragmentManager.OnBackStackChangedListener onBackStackChangedListener =
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (FragmentsStackManager.this.navigationLevel == navigationLevel) {
                            if (backStackEntryCount != activity.getSupportFragmentManager().getBackStackEntryCount()) {
                                return;
                            }

                            backStack.pop();
                            FragmentsStackManager.this.navigationLevel = lastNavigationLevel;
                            activity.getSupportFragmentManager().removeOnBackStackChangedListener(this);
                        }
                    }
                };
        activity.getSupportFragmentManager().addOnBackStackChangedListener(
                onBackStackChangedListener);
        backStack.push(getCurrentFragment());
    }

    @Override
    public void replaceFragment(int navigationLevel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Fragment getLatestBackStackFragment() {
        return backStack.top();
    }

    @Override
    public Fragment getCurrentFragment() {
        return activity.getSupportFragmentManager().findFragmentById(contentId);
    }

    @Override
    public void replaceCurrentFragmentWithoutAddingToBackStack(Fragment newFragment) {
        if (!backStack.isEmpty()) {
            backStack.replaceTop(newFragment);
        }
        Fragments.replaceFragment(activity, contentId, newFragment);
    }
}
