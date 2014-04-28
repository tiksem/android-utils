package com.utilsframework.android.threading;

/**
 * User: Tikhonenko.S
 * Date: 22.11.13
 * Time: 17:28
 */
public interface ThrowingRunnable<T extends Throwable> {
    public void run() throws T;
}
