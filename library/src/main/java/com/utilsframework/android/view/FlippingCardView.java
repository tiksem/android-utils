package com.utilsframework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import com.utilsframework.android.R;

/**
 * User: Tikhonenko.S
 * Date: 15.05.14
 * Time: 14:22
 */
public class FlippingCardView extends FrameLayout{
    private View currentView;
    private boolean displayingFrontView = true;
    private OnFlippingFinished onFlippingFinished;
    private int flippingDuration = 1000;

    public interface OnFlippingFinished {
        void onFlippingFinished();
    }

    public FlippingCardView(Context context) {
        super(context);
    }

    public FlippingCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlippingCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void switchView(){
        Log.i("FlippingCardView", "switchView");

        Animation toMiddleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.flip_to_middle);
        toMiddleAnimation.setDuration(flippingDuration);
        toMiddleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                currentView.setVisibility(INVISIBLE);

                if(displayingFrontView){
                    currentView = getBackView();
                } else {
                    currentView = getFrontView();
                }

                currentView.setVisibility(VISIBLE);
                displayingFrontView = !displayingFrontView;

                Animation fromMiddleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.flip_from_middle);
                fromMiddleAnimation.setDuration(flippingDuration);
                fromMiddleAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(onFlippingFinished != null){
                            onFlippingFinished.onFlippingFinished();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                startAnimation(fromMiddleAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(toMiddleAnimation);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (displayingFrontView) {
            currentView = getFrontView();
            currentView.setVisibility(VISIBLE);
            getBackView().setVisibility(INVISIBLE);
        } else {
            currentView = getBackView();
            currentView.setVisibility(VISIBLE);
            getFrontView().setVisibility(INVISIBLE);
        }
    }

    public OnFlippingFinished getOnFlippingFinished() {
        return onFlippingFinished;
    }

    public void setOnFlippingFinished(OnFlippingFinished onFlippingFinished) {
        this.onFlippingFinished = onFlippingFinished;
    }

    public int getFlippingDuration() {
        return flippingDuration;
    }

    public void setFlippingDuration(int flippingDuration) {
        this.flippingDuration = flippingDuration;
    }

    protected View getFrontView(){
        return getChildAt(0);
    }

    protected View getBackView() {
        return getChildAt(1);
    }

    public boolean frontSideIsShown() {
        return getChildAt(0).getVisibility() == VISIBLE;
    }
}
