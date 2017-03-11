package com.utilsframework.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.utilsframework.android.R;

public class LayoutWithAspectRatio extends FrameLayout {
    private float aspectRatio;

    public LayoutWithAspectRatio(Context context) {
        super(context);
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    public LayoutWithAspectRatio(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attr = getTypedArray(context, attrs, R.styleable.LayoutWithAspectRatio);
        if (attr != null) {
            try {
                aspectRatio = attr.getFloat(R.styleable.LayoutWithAspectRatio_aspectRatio, 1.0f);
            } finally {
                attr.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        float width = MeasureSpec.getSize(widthMeasureSpec);
        float height = MeasureSpec.getSize(heightMeasureSpec);

        if (width < height * aspectRatio) {
            width = height * aspectRatio;
        } else {
            height = width / aspectRatio;
        }

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(Math.round(width), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(Math.round(height), MeasureSpec.EXACTLY)
        );
    }
}
