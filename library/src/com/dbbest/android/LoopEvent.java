package com.dbbest.android;

/**
 * User: Tikhonenko.S
 * Date: 24.12.13
 * Time: 17:15
 */
public interface LoopEvent {
    void run(Runnable runnable);

    void stop();

    void pause();

    void resume();

    boolean isPaused();

    boolean isRunning();

    long getDelay();

    void setDelay(long delay);
}
