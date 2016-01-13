package com.utilsframework.android;

import android.content.Context;
import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by Tikhonenko.S on 31.10.13.
 */

/* Note: Don't forget to stop UiLoopEvent when task is finished or use onDestroy/onStop and other callbacks.
* Be careful! Unstopped UiLoopEvent will produce memory leaks.
* */
public class UiLoopEvent implements LoopEvent{
    public static final long DEFAULT_DELAY = 30;

    private Runnable runnable;
    private Handler handler = new Handler();
    private Runnable onUi;
    private boolean isPaused = false;
    private long delay;

    private void runOnUiThread(){
        onUi = new Runnable() {
            @Override
            public void run() {
                if(runnable != null && canRun()){
                    if (!isPaused) {
                        runnable.run();
                    }
                    handler.postDelayed(onUi, delay);
                }
            }
        };

        handler.post(onUi);
    }

    protected boolean canRun() {
        return true;
    }

    // if context is null UiLoopEvent will work globally
    public UiLoopEvent(long delay) {
        setDelay(delay);
    }

    public UiLoopEvent() {
        this(DEFAULT_DELAY);
    }

    @Override
    public long getDelay() {
        return delay;
    }

    @Override
    public void setDelay(long delay) {
        if(delay < 0){
            throw new IllegalArgumentException();
        }

        this.delay = delay;
    }

    @Override
    public void run(Runnable runnable){
        stop();

        this.runnable = runnable;
        if (runnable != null) {
            runOnUiThread();
        }
    }

    @Override
    public void stop(){
       if(onUi != null){
           handler.removeCallbacks(onUi);
       }

        onUi = null;
        runnable = null;
        isPaused = false;
    }

    @Override
    public void pause(){
        isPaused = true;
    }

    @Override
    public void resume(){
        isPaused = false;
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public boolean isRunning(){
        return runnable != null;
    }
}
