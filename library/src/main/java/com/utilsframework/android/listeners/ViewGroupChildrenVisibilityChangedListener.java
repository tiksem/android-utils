package com.utilsframework.android.listeners;

import android.view.View;
import android.view.ViewGroup;
import com.utilsframework.android.listeners.StateChangedListener;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Tikhonenko.S
 * Date: 13.05.14
 * Time: 16:04
 */
public class ViewGroupChildrenVisibilityChangedListener extends StateChangedListener<ViewGroup, Map<View, Integer>>{
    public ViewGroupChildrenVisibilityChangedListener(ViewGroup object) { 
        super(object);
    }

    @Override
    protected void onStateChanged(ViewGroup object, Map<View, Integer> lastState, Map<View, Integer> currentState) {
    }

    @Override
    public Map<View, Integer> getState(ViewGroup viewGroup) {
        Map<View, Integer> result = new LinkedHashMap<View, Integer>();
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            result.put(view, view.getVisibility());
        }

        return result;
    }
}
