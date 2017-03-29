package com.utilsframework.android.fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.utilsframework.android.UiLoopEvent;
import com.utilsframework.android.WeakUiLoopEvent;
import com.utilsframework.android.navdrawer.FragmentFactory;
import com.utilsframework.android.view.GuiUtilities;

/**
 * Created by CM on 1/21/2015.
 */
public class Fragments {
    private static final int DEFAULT_TRANSITION = FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

    public static long getLong(Fragment fragment, String key, long defaultValue) {
        Bundle arguments = fragment.getArguments();
        if(arguments == null){
            return defaultValue;
        }

        return arguments.getLong(key, defaultValue);
    }

    public static long getLong(Fragment fragment, String key) {
        Bundle arguments = fragment.getArguments();
        if(arguments == null){
            throw new IllegalStateException("arguments == null");
        }

        return arguments.getLong(key);
    }

    public static int getInt(Fragment fragment, String key) {
        Bundle arguments = fragment.getArguments();
        if(arguments == null){
            throw new IllegalStateException("arguments == null");
        }

        return arguments.getInt(key);
    }

    public static int getInt(Fragment fragment, String key, int defaultValue) {
        Bundle arguments = fragment.getArguments();
        if(arguments == null){
            return defaultValue;
        }

        return arguments.getInt(key, defaultValue);
    }

    public static boolean getBoolean(Fragment fragment, String key) {
        Bundle arguments = fragment.getArguments();
        if(arguments == null){
            return false;
        }

        return arguments.getBoolean(key);
    }

    public static <T extends Parcelable> T getParcelable(Fragment fragment, String key) {
        Bundle arguments = fragment.getArguments();
        if(arguments == null){
            return null;
        }

        return arguments.getParcelable(key);
    }

    public static void executeWhenViewCreated(Fragment fragment, final GuiUtilities.OnViewCreated onViewCreated) {
        if(fragment.getView() != null){
            onViewCreated.onViewCreated(fragment.getView());
            return;
        }

        final WeakUiLoopEvent<Fragment> uiLoopEvent = new WeakUiLoopEvent<>(fragment);
        uiLoopEvent.run(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = uiLoopEvent.get();
                if (fragment.getView() != null) {
                    onViewCreated.onViewCreated(fragment.getView());
                    uiLoopEvent.stop();
                }
            }
        });
    }

    public static int getFragmentContainerId(Fragment fragment) {
        return ((View) fragment.getView().getParent()).getId();
    }

    public static void replaceFragment(FragmentActivity activity, int id, Fragment newFragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        removeFragmentWithId(fragmentManager, id);
        fragmentManager.beginTransaction().
                setTransition(DEFAULT_TRANSITION).
                replace(id, newFragment).commit();
    }

    public interface OnBack {
        void onBack();
    }

    public static void replaceOrAddFragmentAndAddToBackStack(FragmentActivity activity, final int id,
                                                             Fragment newFragment) {
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final Fragment currentFragment = fragmentManager.findFragmentById(id);
        if(currentFragment == null){
            fragmentManager.beginTransaction().add(id, newFragment).
                    setTransition(DEFAULT_TRANSITION).
                    addToBackStack(null).commit();
        } else {
            fragmentManager.beginTransaction().replace(id, newFragment).
                    setTransition(DEFAULT_TRANSITION).
                    addToBackStack(null).commit();
        }
    }

    public static void clearBackStack(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    public static void removeFragmentWithId(FragmentManager fragmentManager, int id) {
        Fragment fragment = fragmentManager.findFragmentById(id);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    public static void clearBackStack(FragmentActivity activity) {
        clearBackStack(activity.getSupportFragmentManager());
    }

    public static <T extends android.support.v4.app.Fragment> T createFragmentWith1Arg(T fragment, String key,
                                                                                       String value) {
        Bundle args = new Bundle();
        args.putString(key, value);
        fragment.setArguments(args);
        return fragment;
    }

    public static <T extends android.support.v4.app.Fragment> T createFragmentWith1Arg(T fragment, String key,
                                                                                       Parcelable value) {
        Bundle args = new Bundle();
        args.putParcelable(key, value);
        fragment.setArguments(args);
        return fragment;
    }

    public static <T extends android.support.v4.app.Fragment> T createFragmentWith1Arg(T fragment, String key,
                                                                                       long value) {
        Bundle args = new Bundle();
        args.putLong(key, value);
        fragment.setArguments(args);
        return fragment;
    }

    public static <T extends android.support.v4.app.Fragment> T createFragmentWith1Arg(T fragment, String key,
                                                                                       int value) {
        Bundle args = new Bundle();
        args.putInt(key, value);
        fragment.setArguments(args);
        return fragment;
    }

    public static android.support.v4.app.Fragment getLatestFragmentInBackStack(FragmentActivity activity) {
        android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
        android.support.v4.app.FragmentManager.BackStackEntry backEntry =
                fragmentManager.getBackStackEntryAt(
                        fragmentManager.getBackStackEntryCount() - 1);
        String str = backEntry.getName();
        return fragmentManager.findFragmentByTag(str);
    }
}
