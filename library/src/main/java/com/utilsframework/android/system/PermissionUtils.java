package com.utilsframework.android.system;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.utilsframework.android.view.Toasts;

/**
 * Created by stikhonenko on 1/3/16.
 */
public class PermissionUtils {
    public static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;

    private static boolean shouldRequestPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }

        return ContextCompat.checkSelfPermission(context, permission) !=
                PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void requestPermission(Activity activity, int requestCode,
                                          int description) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toasts.message(activity, description);
        }

        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_STORAGE_PERMISSION_REQUEST_CODE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void requestReadStoragePermission(Activity activity, int description) {
        requestPermission(activity, READ_STORAGE_PERMISSION_REQUEST_CODE, description);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean shouldRequestReadStoragePermission(Context context) {
        return shouldRequestPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}
