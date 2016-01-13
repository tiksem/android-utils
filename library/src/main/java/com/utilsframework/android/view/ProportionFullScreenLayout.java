package com.utilsframework.android.view;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.utilsframework.android.Screen;

/**
 * User: Tikhonenko.S
 * Date: 25.04.14
 * Time: 13:32
 */
public class ProportionFullScreenLayout extends FrameLayout{
    private float proportionWidth = 0.5f;
    private float proportionHeight = 0.5f;
    private int mainOrientation = Configuration.ORIENTATION_LANDSCAPE;

    public ProportionFullScreenLayout(Context context) {
        this(context, null);
    }

    public ProportionFullScreenLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProportionFullScreenLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public float getProportionWidth() {
        return proportionWidth;
    }

    public void setProportionWidth(float proportionWidth) {
        this.proportionWidth = proportionWidth;
        requestLayout();
    }

    public float getProportionHeight() {
        return proportionHeight;
    }

    public void setProportionHeight(float proportionHeight) {
        this.proportionHeight = proportionHeight;
        requestLayout();
    }

    public int getMainOrientation() {
        return mainOrientation;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int orientation = getContext().getResources().getConfiguration().orientation;
        int width = -1;
        int height = -1;

        int screenWidth = Screen.getScreenWidth(getContext());
        int screenHeight = Screen.getScreenHeight(getContext());

        if(orientation == mainOrientation){
            width = Math.round(screenWidth * proportionWidth);
            height = Math.round(screenHeight * proportionHeight);
        } else {
            width = Math.round(screenHeight * proportionWidth);
            height = Math.round(screenHeight * proportionHeight);
        }

        setMeasuredDimension(width, height);
    }

    public void setMainOrientation(int mainOrientation) {
        if(mainOrientation != Configuration.ORIENTATION_LANDSCAPE &&
                mainOrientation != Configuration.ORIENTATION_PORTRAIT){
            throw new IllegalArgumentException();
        }

        this.mainOrientation = mainOrientation;
        requestLayout();
    }
}
