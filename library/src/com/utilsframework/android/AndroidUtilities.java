package com.utilsframework.android;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by CM on 6/17/2015.
 */
public class AndroidUtilities {
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void startActivity(Context context, Class<? extends Activity> aClass) {
        Intent intent = new Intent(context, aClass);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Activity activity, Class<? extends Activity> aClass, int requestCode) {
        Intent intent = new Intent(activity, aClass);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(Fragment fragment, Class<? extends Activity> aClass, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), aClass);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(android.support.v4.app.Fragment fragment, Class<? extends Activity> aClass, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), aClass);
        fragment.startActivityForResult(intent, requestCode);
    }
}
