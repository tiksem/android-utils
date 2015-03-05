package com.utilsframework.android.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcelable;
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

    public static int getFragmentContainerId(Fragment fragment) {
        return ((View) fragment.getView().getParent()).getId();
    }

    public static void replaceFragment(Activity activity, int id, Fragment newFragment) {
        FragmentManager fragmentManager = activity.getFragmentManager();
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

    public static void clearBackStack(FragmentManager fragmentManager) {
        fragmentManager.popBackStackImmediate();
    }

    public static void removeFragmentWithId(FragmentManager fragmentManager, int id) {
        Fragment fragment = fragmentManager.findFragmentById(id);
        fragmentManager.beginTransaction().remove(fragment).commit();
    }

    public static void clearBackStack(Activity activity) {
        clearBackStack(activity.getFragmentManager());
    }
}
