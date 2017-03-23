package com.utilsframework.android.network;

import android.os.AsyncTask;
import com.utilsframework.android.threading.*;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * Created by stykhonenko on 12.10.15.
 */
public class LegacyRequestManager extends BaseRequestManager {
    private Executor executor;

    public <Result> Cancelable execute(final Threading.Task<IOException, Result> task) {
        RequestListener<Result, IOException> listener = new RequestListener<Result, IOException>() {
            @Override
            public void onSuccess(Result result) {
                task.onComplete(result, null);
            }

            @Override
            public void onError(IOException e) {
                task.onComplete(null, e);
            }

            @Override
            public void onCanceled() {
                task.onCancelled(null, null);
            }

            @Override
            public void onAfterCompleteOrCanceled() {
                task.onAfterCompleteOrCancelled();
            }
        };

        ResultTask<Result, IOException> resultTask = new ResultTask<Result, IOException>(this, listener) {
            @Override
            protected Result getResultInBackground() throws IOException {
                return task.runOnBackground();
            }
        };

        if (executor != null) {
            resultTask.executeOnExecutor(executor);
        } else {
            resultTask.execute();
        }

        return resultTask;
    }

    public LegacyRequestManager(Executor executor) {
        this.executor = executor;
    }

    public LegacyRequestManager() {
    }

    public void execute(final ThrowingRunnable<IOException> runnable, final OnFinish<IOException> onFinish) {
        execute(new Threading.Task<IOException, Object>() {
            @Override
            public Object runOnBackground() throws IOException {
                runnable.run();
                return null;
            }

            @Override
            public void onComplete(Object o, IOException error) {
                if (onFinish != null) {
                    onFinish.onFinish(error);
                }
            }
        });
    }

    public void execute(final Runnable runnable) {
        execute(new Threading.Task<IOException, Object>() {
            @Override
            public Object runOnBackground() throws IOException {
                runnable.run();
                return null;
            }

            @Override
            public void onComplete(Object o, IOException error) {

            }
        });
    }
}
