package com.utilsframework.android.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.utilsframework.android.UiLoopEvent;
import com.utilsframework.android.view.GuiUtilities;

/**
 * Created by CM on 1/21/2015.
 */
public class Fragments {
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

    // use replaceFragment instead
    @Deprecated
    public static void addFragment(Activity activity, int containerId, Fragment fragment) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction().add(containerId, fragment).commit();
    }

    public static void executeWhenViewCreated(final Fragment fragment, final GuiUtilities.OnViewCreated onViewCreated) {
        if(fragment.getView() != null){
            onViewCreated.onViewCreated(fragment.getView());
            return;
        }

        final UiLoopEvent uiLoopEvent = new UiLoopEvent(fragment);
        uiLoopEvent.run(new Runnable() {
            @Override
            public void run() {
                if (fragment.getView() != null) {
                    onViewCreated.onViewCreated(fragment.getView());
                    uiLoopEvent.stop();
                }
            }
        });
    }

    public static void executeWhenViewCreated(final android.support.v4.app.Fragment fragment,
                                              final GuiUtilities.OnViewCreated onViewCreated) {
        if(fragment.getView() != null){
            onViewCreated.onViewCreated(fragment.getView());
            return;
        }

        final UiLoopEvent uiLoopEvent = new UiLoopEvent(fragment);
        uiLoopEvent.run(new Runnable() {
            @Override
            public void run() {
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

    public static void replaceFragment(Activity activity, int id, Fragment newFragment) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction().replace(id, newFragment).commit();
    }

    public static void replaceFragment(FragmentActivity activity, int id, android.support.v4.app.Fragment newFragment) {
        android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(id, newFragment).commit();
    }

    public interface OnBack {
        void onBack();
    }

    public static FragmentManager.OnBackStackChangedListener
    replaceFragmentAndAddToBackStack(Activity activity, final int id,
                                                        Fragment newFragment, final OnBack onBack) {
        final FragmentManager fragmentManager = activity.getFragmentManager();
        final Fragment currentFragment = fragmentManager.findFragmentById(id);
        if(currentFragment == null){
            throw new IllegalStateException("Unable to replace fragment, fragment doesn't exist");
        }

        FragmentManager.OnBackStackChangedListener result = null;
        if (onBack != null) {
            final int count = fragmentManager.getBackStackEntryCount();
            result = new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    int backStackEntryCount = fragmentManager.getBackStackEntryCount();
                    if(fragmentManager.findFragmentById(id) == currentFragment && count == backStackEntryCount) {
                        onBack.onBack();
                    }

                    if(backStackEntryCount <= count){
                        fragmentManager.removeOnBackStackChangedListener(this);
                    }
                }
            };
            fragmentManager.addOnBackStackChangedListener(result);
        }

        fragmentManager.beginTransaction().replace(id, newFragment).addToBackStack(null).commit();
        return result;
    }

    public static android.support.v4.app.FragmentManager.OnBackStackChangedListener
    replaceFragmentAndAddToBackStack(AppCompatActivity activity, final int id,
                                     android.support.v4.app.Fragment newFragment, final OnBack onBack) {
        final android.support.v4.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final android.support.v4.app.Fragment currentFragment = fragmentManager.findFragmentById(id);
        if(currentFragment == null){
            throw new IllegalStateException("Unable to replace fragment, fragment doesn't exist");
        }

        android.support.v4.app.FragmentManager.OnBackStackChangedListener result = null;
        if (onBack != null) {
            final int count = fragmentManager.getBackStackEntryCount();
            result = new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    int backStackEntryCount = fragmentManager.getBackStackEntryCount();
                    if(fragmentManager.findFragmentById(id) == currentFragment && count == backStackEntryCount) {
                        onBack.onBack();
                    }

                    if(backStackEntryCount <= count){
                        fragmentManager.removeOnBackStackChangedListener(this);
                    }
                }
            };
            fragmentManager.addOnBackStackChangedListener(result);
        }

        fragmentManager.beginTransaction().replace(id, newFragment).addToBackStack(null).commit();
        return result;
    }


    public static void clearBackStack(FragmentManager fragmentManager) {
        fragmentManager.popBackStackImmediate();
    }

    public static void clearBackStack(android.support.v4.app.FragmentManager fragmentManager) {
        fragmentManager.popBackStackImmediate();
    }

    public static void removeFragmentWithId(FragmentManager fragmentManager, int id) {
        Fragment fragment = fragmentManager.findFragmentById(id);
        fragmentManager.beginTransaction().remove(fragment).commit();
    }

    public static void removeFragmentWithId(android.support.v4.app.FragmentManager fragmentManager, int id) {
        android.support.v4.app.Fragment fragment = fragmentManager.findFragmentById(id);
        fragmentManager.beginTransaction().remove(fragment).commit();
    }

    public static void clearBackStack(Activity activity) {
        clearBackStack(activity.getFragmentManager());
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
}
