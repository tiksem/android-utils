package com.utilsframework.android.view.animation;

import android.view.View;
import android.view.animation.TranslateAnimation;
import com.utilsframework.android.Screen;

/**
 * User: Tikhonenko.S
 * Date: 25.04.14
 * Time: 14:09
 */
public class AnimationUtilities {
    public static TranslateAnimation slideDownToCenterFullScreenAnimation(View view) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        int screenWidth = Screen.getScreenWidth(view.getContext());
        int screenHeight = Screen.getScreenHeight(view.getContext());

        int startX = (screenWidth - width) / 2;
        int startY = -height;
        int endX = startX;
        int endY = (screenHeight - height) / 2;

        view.setX(startX);
        view.setY(startY);

        return new TranslateAnimation(startX, endX, startY, endY);
    }

    public static TranslateAnimation slideUpFromCenterFullScreenAnimation(View view) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        int screenWidth = Screen.getScreenWidth(view.getContext());
        int screenHeight = Screen.getScreenHeight(view.getContext());

        int endX = (screenWidth - width) / 2;
        int endY = -height;
        int startX = endX;
        int startY = (screenHeight - height) / 2;

        view.setX(startX);
        view.setY(startY);

        return new TranslateAnimation(startX, endX, startY, endY);
    }
}
