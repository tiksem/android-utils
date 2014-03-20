package com.dbbest.android;

/**
 * User: Tikhonenko.S
 * Date: 24.12.13
 * Time: 17:15
 */
public interface LoopEvent extends RunningLoopEvent{
    void run(Runnable runnable);
    void stop();
}
