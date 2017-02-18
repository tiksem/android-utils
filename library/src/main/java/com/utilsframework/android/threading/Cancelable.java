package com.utilsframework.android.threading;

/**
 * User: Tikhonenko.S
 * Date: 24.12.13
 * Time: 18:26
 */
public interface Cancelable {
    void cancel(boolean interrupt);
    boolean isCancelled();
}
