package com.utilsframework.android.view;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.*;

/**
 * User: Tikhonenko.S
 * Date: 16.12.13
 * Time: 14:30
 */
public class MultiClicksResolver {
    private Set<View> views;
    private Map<View, View.OnClickListener> clickListenerMap = new HashMap<View, View.OnClickListener>();
    private int touchesDownCount = 0;

    private void initClickListeners(){
        for(final View view : views){
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();

                    if(action == MotionEvent.ACTION_DOWN){
                        touchesDownCount++;
                    } else if (action == MotionEvent.ACTION_UP){
                        if (touchesDownCount <= 1) {
                            View.OnClickListener onClickListener = clickListenerMap.get(v);
                            if(onClickListener != null){
                                onClickListener.onClick(v);
                            }
                        }

                        touchesDownCount--;
                    } else if(action == MotionEvent.ACTION_CANCEL) {
                        touchesDownCount--;
                    }

                    return false;
                }
            });
        }
    }

    public MultiClicksResolver(ViewGroup viewGroup) {
        views = new HashSet<View>(GuiUtilities.getNonViewGroupChildrenRecursive(viewGroup));
        initClickListeners();
    }

    public MultiClicksResolver(Activity activity){
        this((ViewGroup) activity.getWindow().getDecorView().getRootView());
    }

    public void setOnClickListener(View view, View.OnClickListener onClickListener){
        if(!views.contains(view)){
            throw new NoSuchElementException("view is not registered ViewGroup or Activity");
        }

        if (onClickListener != null) {
            clickListenerMap.put(view, onClickListener);
        } else {
            clickListenerMap.remove(view);
        }
    }


}
