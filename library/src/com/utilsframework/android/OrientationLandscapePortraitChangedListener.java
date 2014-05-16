package com.utilsframework.android;

import android.content.Context;
import android.content.res.Configuration;

/**
 * User: Tikhonenko.S
 * Date: 16.04.14
 * Time: 17:48
 */
public abstract class OrientationLandscapePortraitChangedListener {
    private final UiLoopEvent uiLoopEvent;
    private int lastOrientation;
    private Context context;

    protected OrientationLandscapePortraitChangedListener(final Context context, Object parent) {
        if(parent == null){
            parent = context;
        }

        uiLoopEvent = new UiLoopEvent(parent);
        this.context = context;
    }

    protected OrientationLandscapePortraitChangedListener(final Context context) {
        this(context, null);
    }

    public void start(){
        lastOrientation = context.getResources().getConfiguration().orientation;

        uiLoopEvent.run(new Runnable() {
            @Override
            public void run() {
                int currentOrientation = context.getResources().getConfiguration().orientation;

                if (currentOrientation != lastOrientation) {
                    if(currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
                        onLandscape();
                    } else {
                        onPortrait();
                    }

                    lastOrientation = currentOrientation;
                }
            }
        });
    }

    public void stop(){
        uiLoopEvent.stop();
    }

    public void pause(){
        uiLoopEvent.pause();
    }

    public void resume(){
        uiLoopEvent.resume();
    }

    public abstract void onLandscape();
    public abstract void onPortrait();
}
