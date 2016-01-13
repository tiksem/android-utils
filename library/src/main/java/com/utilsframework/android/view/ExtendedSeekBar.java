package com.utilsframework.android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;
import com.utils.framework.Reflection;

public class ExtendedSeekBar extends SeekBar {

    public ExtendedSeekBar(Context context) {
        super(context);
    }
    public ExtendedSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ExtendedSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Drawable getSeekBarThumb() {
        return (Drawable) Reflection.getFieldValueRecursive(this, "mThumb");
    }
}