package com.utilsframework.android.view;

import android.widget.ProgressBar;
import com.utilsframework.android.UiLoopEventWithTimeTracking;

/**
 * User: Tikhonenko.S
 * Date: 03.12.13
 * Time: 13:47
 */
public abstract class ProgressBarUpdater {
    private static final long UPDATE_DELAY = 10;

    private ProgressBar progressBar;
    private UiLoopEventWithTimeTracking updater;

    public ProgressBarUpdater(final ProgressBar progressBar) {
        this.progressBar = progressBar;
        updater = new UiLoopEventWithTimeTracking(progressBar, UPDATE_DELAY);
    }

    public void start(){
        updater.run(new Runnable() {
            @Override
            public void run() {
                progressBar.setMax(getMax());
                int progress = (int) updater.getWorkingTime();
                progressBar.setProgress(progress);
            }
        });
    }

    public void pause(){
        updater.pause();
    }

    public void resume() {
        updater.resume();
    }

    public void startOrResume(){
        if (updater.isRunning()) {
            updater.resume();
        } else {
            start();
        }
    }

    public void stop(){
        updater.stop();
    }

    public void reset() {
        updater.reset();
    }

    public abstract int getMax();
}
