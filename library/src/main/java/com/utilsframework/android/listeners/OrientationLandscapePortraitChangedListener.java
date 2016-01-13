package com.utilsframework.android.listeners;

import android.content.Context;
import android.content.res.Configuration;
import com.utils.framework.Destroyable;
import com.utilsframework.android.UiLoopEvent;
import com.utilsframework.android.WeakUiLoopEvent;

/**
 * User: Tikhonenko.S
 * Date: 16.04.14
 * Time: 17:48
 */
public abstract class OrientationLandscapePortraitChangedListener implements Destroyable {
    private final WeakUiLoopEvent<Context> uiLoopEvent;
    private int lastOrientation;

    protected OrientationLandscapePortraitChangedListener(final Context context) {
        uiLoopEvent = new WeakUiLoopEvent<Context>(context);
    }

    public void start(){
        Context context = uiLoopEvent.get();
        if (context == null) {
            return;
        }

        lastOrientation = context.getResources().getConfiguration().orientation;

        uiLoopEvent.run(new Runnable() {
            @Override
            public void run() {
                int currentOrientation = uiLoopEvent.get().getResources().getConfiguration().orientation;

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

    @Override
    public void destroy() {
        uiLoopEvent.stop();
    }
}
