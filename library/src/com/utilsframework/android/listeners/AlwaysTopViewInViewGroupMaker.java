package com.utilsframework.android.listeners;

import android.view.View;
import android.view.ViewGroup;
import com.utilsframework.android.listeners.StateChangedListener;

/**
 * User: Tikhonenko.S
 * Date: 30.04.14
 * Time: 19:00
 */
public class AlwaysTopViewInViewGroupMaker extends StateChangedListener<View, Boolean>{
    public AlwaysTopViewInViewGroupMaker(View view){
        super(view);
    }

    @Override
    protected void onStateChanged(View view, Boolean lastState, Boolean currentState) {
        if(getState(view)){
            return;
        }

        ViewGroup viewGroup = (ViewGroup) view.getParent();

        viewGroup.removeView(view);
        viewGroup.addView(view);
    }

    @Override
    public Boolean getState(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        return viewGroup.getChildAt(viewGroup.getChildCount() - 1) == view;
    }
}
