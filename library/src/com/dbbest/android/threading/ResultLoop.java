package com.dbbest.android.threading;

/**
 * User: Tikhonenko.S
 * Date: 24.12.13
 * Time: 17:46
 */
public interface ResultLoop<T> {
    boolean resultIsReady();
    T getResult();
    public void handleResult(T result);
    public void onTimeIsUp();
}
