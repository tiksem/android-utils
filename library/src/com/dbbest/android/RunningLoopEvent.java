package com.dbbest.android;

/**
 * User: Tikhonenko.S
 * Date: 19.03.14
 * Time: 19:13
 */
public interface RunningLoopEvent {
    void pause();

    void resume();

    boolean isPaused();

    boolean isRunning();

    long getDelay();

    void setDelay(long delay);
}
