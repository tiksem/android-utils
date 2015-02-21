package com.utilsframework.android.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.utilsframework.android.Screen;

/**
 * Created by CM on 2/21/2015.
 */
public class FullScreenLayout extends FrameLayout {
    public FullScreenLayout(Context context) {
        super(context);
    }

    public FullScreenLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Point screenSize = Screen.getContentSize((Activity) getContext());

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(screenSize.x, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(screenSize.y, MeasureSpec.EXACTLY)
        );
    }
}
