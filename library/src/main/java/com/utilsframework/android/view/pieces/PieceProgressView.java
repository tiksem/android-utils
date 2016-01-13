package com.utilsframework.android.view.pieces;

import android.content.Context;
import android.util.AttributeSet;
import com.utilsframework.android.UiLoopEvent;

/**
 * User: Tikhonenko.S
 * Date: 05.11.13
 * Time: 15:03
 */
public class PieceProgressView extends StaticPieceProgressView {
    private UiLoopEvent fragmentsUpdating;

    public PieceProgressView(Context context) {
        super(context);
    }

    public PieceProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PieceProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void pauseFragmentsUpdating(){
        fragmentsUpdating.pause();
    }

    public void resumeFragmentsUpdating(){
        fragmentsUpdating.resume();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        fragmentsUpdating = new UiLoopEvent();
        fragmentsUpdating.run(new Runnable() {
            @Override
            public void run() {
                updateFragments();
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        fragmentsUpdating.stop();
    }
}
