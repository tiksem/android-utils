package com.utilsframework.android.view;

import android.view.View;
import com.utils.framework.patterns.StateProvider;

/**
 * User: Tikhonenko.S
 * Date: 26.12.13
 * Time: 15:21
 */
class ViewVisibilityStateProvider implements StateProvider<View, Integer> {
    @Override
    public Integer getState(View view) {
        return view.getVisibility();
    }

    @Override
    public void restoreState(View view, Integer visibility) {
        view.setVisibility(visibility);
    }
}
