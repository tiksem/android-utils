package com.dbbest.android;

import android.content.Context;
import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by Tikhonenko.S on 31.10.13.
 */
public class UiLoopEvent {
    private static final long DEFAULT_DELAY = 30;

    private Runnable runnable;
    private Handler handler = new Handler();
    private Runnable onUi;
    private boolean isPaused = false;
    private long delay;
    private WeakReference<Context> context;

    private void runOnUiThread(){
        onUi = new Runnable() {
            @Override
            public void run() {
                if(runnable != null && (context == null || context.get() != null)){
                    if (!isPaused) {
                        runnable.run();
                    }
                    handler.postDelayed(onUi, delay);
                }
            }
        };

        handler.post(onUi);
    }

    // if context is null UiLoopEvent will work globally
    public UiLoopEvent(Context context, long delay) {
        setDelay(delay);
        if (context != null) {
            this.context = new WeakReference<Context>(context);
        }
    }

    public UiLoopEvent(Context context) {
        this(context, DEFAULT_DELAY);
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        if(delay < 0){
            throw new IllegalArgumentException();
        }

        this.delay = delay;
    }

    public void run(Runnable runnable){
        if(onUi != null){
            handler.removeCallbacks(onUi);
        }

        this.runnable = runnable;
        if (runnable != null) {
            runOnUiThread();
        }
    }

    public void stop(){
       if(onUi != null){
           handler.removeCallbacks(onUi);
       }

        onUi = null;
        runnable = null;
    }

    public void pause(){
        isPaused = true;
    }

    public void resume(){
        isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isRunning(){
        return runnable != null;
    }
}
