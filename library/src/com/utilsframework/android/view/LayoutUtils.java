package com.utilsframework.android.view;

import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by CM on 6/19/2015.
 */
public class LayoutUtils {
    public static ViewGroup.LayoutParams wrapContent() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
