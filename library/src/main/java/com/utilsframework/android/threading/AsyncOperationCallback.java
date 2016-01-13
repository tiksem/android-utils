package com.utilsframework.android.threading;

/**
 * Created by CM on 10/5/2014.
 */
public interface AsyncOperationCallback<T> {
    T runOnBackground();
    void onFinish(T result);
}
