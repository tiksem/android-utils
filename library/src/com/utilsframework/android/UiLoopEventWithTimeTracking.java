package com.utilsframework.android;

import android.content.Context;

/**
 * User: Tikhonenko.S
 * Date: 03.12.13
 * Time: 13:52
 */
public class UiLoopEventWithTimeTracking extends UiLoopEvent{
    private long startTime = -1;
    private long endTime = -1;
    private long pauseTimeSum = 0;
    private long pauseTime = -1;

    public UiLoopEventWithTimeTracking(Object context, long delay) {
        super(context, delay);
    }

    public UiLoopEventWithTimeTracking(Context context) {
        super(context);
    }

    @Override
    public void stop() {
        if (endTime < 0) {
            super.stop();
            endTime = System.currentTimeMillis();
        }
    }

    @Override
    public void pause() {
        if (!isPaused() && isRunning()) {
            super.pause();
            pauseTime = System.currentTimeMillis();
        }
    }

    @Override
    public void resume() {
        if (isPaused()) {
            super.resume();
            long currentTime = System.currentTimeMillis();
            pauseTimeSum += currentTime - pauseTime;
        }
    }

    @Override
    public void run(Runnable runnable) {
        super.run(runnable);
        startTime = System.currentTimeMillis();
        endTime = -1;
        pauseTimeSum = 0;
        pauseTime = -1;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getWorkingTime() {
        if(startTime < 0){
            return 0;
        }

        long currentTime;
        if(endTime >= 0){
            currentTime = endTime;
        } else {
            currentTime = System.currentTimeMillis();
        }

        return currentTime - startTime - pauseTimeSum;
    }

    public long getLastPauseTime() {
        return pauseTime;
    }
}
