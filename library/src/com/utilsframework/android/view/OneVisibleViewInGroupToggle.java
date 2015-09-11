package com.utilsframework.android.view;

import android.view.View;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by CM on 9/11/2015.
 */
public class OneVisibleViewInGroupToggle {
    private Set<View> views;

    public OneVisibleViewInGroupToggle(View visible, View... views) {
        this.views = new LinkedHashSet<>(Arrays.asList(views));
        this.views.add(visible);
        makeVisible(visible);
    }

    public void makeVisible(View visibleView) {
        if (!views.contains(visibleView)) {
            throw new IllegalArgumentException("View doesn't exist");
        }

        for (View view : views) {
            if (view == visibleView) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }
}
