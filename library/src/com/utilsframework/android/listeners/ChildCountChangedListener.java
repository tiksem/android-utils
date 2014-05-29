package com.utilsframework.android.listeners;

import android.view.ViewGroup;
import com.utilsframework.android.listeners.StateChangedListener;

/**
 * User: Tikhonenko.S
 * Date: 30.04.14
 * Time: 18:49
 */
public abstract class ChildCountChangedListener extends StateChangedListener<ViewGroup, Integer>{
    protected ChildCountChangedListener(ViewGroup object, Object context) {
        super(object, context);
    }

    public ChildCountChangedListener(ViewGroup viewGroup) {
        super(viewGroup);
    }

    @Override
    protected final void onStateChanged(ViewGroup viewGroup, Integer lastChildCount, Integer currentChildCount) {
        if(lastChildCount == viewGroup.getChildCount()){
            throw new IllegalStateException();
        }

        if(lastChildCount > viewGroup.getChildCount()){
            onViewAdded();
        } else {
            onViewRemoved();
        }
    }

    @Override
    public Integer getState(ViewGroup viewGroup) {
        return viewGroup.getChildCount();
    }

    protected abstract void onViewRemoved();
    protected abstract void onViewAdded();
}
