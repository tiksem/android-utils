package com.utilsframework.android.fragments;

import android.app.Fragment;
import android.os.Bundle;

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
}
