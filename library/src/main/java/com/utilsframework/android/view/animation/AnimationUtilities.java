package com.utilsframework.android.view.animation;

import android.graphics.Point;
import android.view.View;
import android.view.animation.*;
import com.utils.framework.CollectionUtils;
import com.utils.framework.predicates.InstanceOfPredicate;
import com.utilsframework.android.Screen;
import com.utilsframework.android.view.GuiUtilities;

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

    public interface OnAnimationFinished {
        void onAnimationFinished();
    }

    public static void showAnimationAndRemoveView(final View view, int animationId,
                                                  int duration,
                                                  final OnAnimationFinished onAnimationFinished) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), animationId);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(onAnimationFinished != null){
                    onAnimationFinished.onAnimationFinished();
                }
                GuiUtilities.removeView(view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.setVisibility(View.VISIBLE);

        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    public static RotateAnimation rotateAnimationAroundViewCenter(View view) {
        Point viewCenter = GuiUtilities.getViewCenter(view);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, viewCenter.x, viewCenter.y);
        return rotateAnimation;
    }
}
