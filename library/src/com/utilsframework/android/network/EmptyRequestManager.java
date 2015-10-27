package com.utilsframework.android.network;

import android.os.AsyncTask;
import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.Threading;
import com.utilsframework.android.threading.ThrowingRunnable;

import java.io.IOException;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class EmptyRequestManager implements RequestManager {
    @Override
    public <Result> AsyncTask execute(Threading.Task<IOException, Result> task) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void execute(ThrowingRunnable<IOException> runnable, OnFinish<IOException> onFinish) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancelAll() {

    }

    @Override
    public void execute(Runnable runnable) {
        throw new UnsupportedOperationException();
    }
}
