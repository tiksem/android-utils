package com.utilsframework.android.view;

import android.view.View;
import com.utils.framework.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by CM on 9/11/2015.
 */
public class OneVisibleViewInGroupToggle {
    private Set<View> views;
    private View visible;

    public OneVisibleViewInGroupToggle(View... views) {
        this.views = CollectionUtils.asLinkedHashSetRemoveNulls(views);
        if (this.views.isEmpty()) {
            throw new NullPointerException("You need at least one non-null view");
        }

        visible = this.views.iterator().next();

        if (visible == null) {
            throw new NullPointerException("visible view should not be null");
        }

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

        visible = visibleView;
    }

    public View getVisible() {
        return visible;
    }
}
