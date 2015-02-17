package com.utilsframework.android.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
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

    public static void replaceFragmentAndAddToBackStack(Activity activity, int id, Fragment newFragment) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction().replace(id, newFragment).addToBackStack(null).commit();
    }

    public static void clearBackStack(FragmentManager fragmentManager) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void clearBackStack(Activity activity) {
        clearBackStack(activity.getFragmentManager());
    }
}
