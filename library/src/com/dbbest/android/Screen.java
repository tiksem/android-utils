package com.dbbest.android;

import android.app.Activity;
import android.util.DisplayMetrics;
import com.dbbest.framework.MathUtils;

/**
 * User: Tikhonenko.S
 * Date: 04.12.13
 * Time: 13:46
 */
public final class Screen {
    public static DisplayMetrics getSize(Activity activity){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics;
    }

    public static DisplayMetrics getAspectRatio(Activity activity){
        DisplayMetrics size = getSize(activity);
        int gcd = MathUtils.getGreatestCommonDivisor(size.widthPixels, size.heightPixels);
        size.widthPixels /= gcd;
        size.heightPixels /= gcd;
        return size;
    }

    public static int getWidthFromHeight(Activity activity, int height){
        DisplayMetrics displayMetrics = getSize(activity);
        int width =  height * displayMetrics.widthPixels / displayMetrics.heightPixels;
        if(width % 2 == 1){
            width--;
        }

        return width;
    }
}
