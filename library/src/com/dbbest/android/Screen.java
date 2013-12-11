package com.dbbest.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.dbbest.framework.MathUtils;

/**
 * User: Tikhonenko.S
 * Date: 04.12.13
 * Time: 13:46
 */
public final class Screen {

    public static DisplayMetrics getAspectRatio(Context context){
        DisplayMetrics size = getDisplayMetrics(context);
        int gcd = MathUtils.getGreatestCommonDivisor(size.widthPixels, size.heightPixels);
        size.widthPixels /= gcd;
        size.heightPixels /= gcd;
        return size;
    }

    public static int getWidthFromHeight(Context activity, int height){
        DisplayMetrics displayMetrics = getDisplayMetrics(activity);
        int width =  height * displayMetrics.widthPixels / displayMetrics.heightPixels;
        if(width % 2 == 1){
            width--;
        }

        return width;
    }

    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect rectgle= new Rect();
        Window window= activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
        int statusBarHeight = rectgle.top;
//        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
//        int titleBarHeight = contentViewTop - statusBarHeight;
        return statusBarHeight;
    }

    public static DisplayMetrics getDisplayMetrics(Context context){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(displaymetrics);
        return displaymetrics;
    }
}
