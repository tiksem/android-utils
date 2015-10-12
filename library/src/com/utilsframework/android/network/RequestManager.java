package com.utilsframework.android.network;

import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.Threading;
import com.utilsframework.android.threading.ThrowingRunnable;

import java.io.IOException;

/**
 * Created by stykhonenko on 12.10.15.
 */
public interface RequestManager {
    <Result> void execute(Threading.Task<IOException, Result> task);
    void execute(ThrowingRunnable<IOException> runnable, OnFinish<IOException> onFinish);
    void cancelAll();
}
