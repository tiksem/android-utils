package com.utilsframework.android.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * User: Tikhonenko.S
 * Date: 24.06.14
 * Time: 16:39
 */
public class SlideDownPushViewAnimator {
    private View upView;
    private View downView;
    private int slideDuration = 1000;
    private float slideDownHeightCoefficient = 1.0f;
    private ObjectAnimator downAnimator;
    private ObjectAnimator upAnimator;

    public enum SlideDirection {
        UP, DOWN, AUTO
    }

    public interface OnSlideFinished {
        void onSlideFinished();
    }

    public SlideDownPushViewAnimator(final View upView, View downView) {
        this.upView = upView;
        this.downView = downView;

        GuiUtilities.executeWhenViewMeasured(upView, new Runnable() {
            @Override
            public void run() {
                upView.setY(-upView.getMeasuredHeight());
            }
        });

        downView.setY(0);
    }

    public int getSlideDuration() {
        return slideDuration;
    }

    public void setSlideDuration(int slideDuration) {
        this.slideDuration = slideDuration;
    }

    public float getSlideDownHeightCoefficient() {
        return slideDownHeightCoefficient;
    }

    public void setSlideDownHeightCoefficient(float slideDownHeightCoefficient) {
        this.slideDownHeightCoefficient = slideDownHeightCoefficient;
    }

    public void slide(SlideDirection slideDirection, final OnSlideFinished onSlideFinished) {
        if(upAnimator != null){
            upAnimator.cancel();
        }

        if(downAnimator != null){
            downAnimator.cancel();
        }

        float currentUpY = upView.getY();
        float currentDownY = downView.getY();
        int upHeight = upView.getMeasuredHeight();

        float endUpY = 0;
        if (slideDirection == SlideDirection.AUTO) {
            if(upHeight + currentUpY + 0.1f >= (upHeight * slideDownHeightCoefficient) / 2){
                endUpY = -upHeight;
            } else {
                endUpY = -upHeight + upHeight * slideDownHeightCoefficient;
            }
        } else if(slideDirection == SlideDirection.DOWN) {
            endUpY = -upHeight + upHeight * slideDownHeightCoefficient;
        } else {
            endUpY = -upHeight;
        }

        float endDownY = 0;
        if (slideDirection == SlideDirection.AUTO) {
            if(currentDownY + 0.1f >= (upHeight * slideDownHeightCoefficient) / 2){
                endDownY = 0;
            } else {
                endDownY = upHeight * slideDownHeightCoefficient;
            }
        } else if(slideDirection == SlideDirection.DOWN) {
            endDownY = upHeight * slideDownHeightCoefficient;
        } else {
            endDownY = 0;
        }

        int duration = slideDuration * Math.round(Math.abs(currentUpY - endUpY) / upHeight / slideDownHeightCoefficient);

        upAnimator = ObjectAnimator.ofFloat(upView, "y", currentUpY, endUpY);
        upAnimator.setDuration(duration);
        upAnimator.start();

        downAnimator = ObjectAnimator.ofFloat(downView, "y", currentDownY, endDownY);
        downAnimator.setDuration(duration);
        downAnimator.start();

        upAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onSlideFinished != null) {
                    onSlideFinished.onSlideFinished();
                }
                upAnimator.removeAllListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
}
