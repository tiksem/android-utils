package com.utilsframework.android.view.fragments;

import android.content.Context;
import android.util.AttributeSet;
import com.utilsframework.android.UiLoopEvent;

/**
 * User: Tikhonenko.S
 * Date: 05.11.13
 * Time: 15:03
 */
public class FragmentsView extends StaticFragmentsView{
    private UiLoopEvent fragmentsUpdating;

    private void init(){
        fragmentsUpdating = new UiLoopEvent(getContext(), 30);
        fragmentsUpdating.run(new Runnable() {
            @Override
            public void run() {
                updateFragments();
            }
        });
    }

    public FragmentsView(Context context) {
        super(context);
        init();
    }

    public FragmentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FragmentsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void pauseFragmentsUpdating(){
        fragmentsUpdating.pause();
    }

    public void resumeFragmentsUpdating(){
        fragmentsUpdating.resume();
    }
}
