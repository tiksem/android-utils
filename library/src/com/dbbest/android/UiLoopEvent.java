package com.dbbest.android;

import android.os.Handler;

/**
 * Created by Tikhonenko.S on 31.10.13.
 */
public class UiLoopEvent {
    private Runnable runnable;
    private Handler handler = new Handler();
    private Runnable onUi;
    private boolean isPaused = false;

    private void runOnUiThread(){
        onUi = new Runnable() {
            @Override
            public void run() {
                if(runnable != null){
                    if (!isPaused) {
                        runnable.run();
                    }
                    handler.post(onUi);
                }
            }
        };

        handler.post(onUi);
    }

    public void run(Runnable runnable){
        if(this.runnable != null){
            handler.removeCallbacks(this.runnable);
        }

        this.runnable = runnable;
        runOnUiThread();
    }

    public void stop(){
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
