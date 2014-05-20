package com.utilsframework.android.view.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import com.dbbest.framework.CollectionUtils;
import com.dbbest.framework.predicates.InstanceOfPredicate;
import com.utilsframework.android.Screen;

import java.util.List;

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

    public static <T extends Animation> T getAnimationSetChildByType(AnimationSet animationSet, Class<T> aClass) {
        List<Animation> animations = animationSet.getAnimations();
        return (T) CollectionUtils.find(animations, new InstanceOfPredicate<Animation>(aClass));
    }
}
