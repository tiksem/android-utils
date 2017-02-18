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
public class AsyncRequestExecutorManager implements LegacyRequestManager {
    private Queue<AsyncTask> runningRequests = new ArrayDeque<>();
    private Executor executor;

    @Override
    public <Result> AsyncTask execute(final Threading.Task<IOException, Result> task) {
        final AsyncTask<Void, Void, Result> asyncTask = new AsyncTask<Void, Void, Result>() {
            IOException error;

            @Override
            protected Result doInBackground(Void... params) {
                try {
                    return task.runOnBackground();
                } catch (IOException e) {
                    error = e;
                    return null;
                }
            }

            @Override
            protected void onCancelled(Result result) {
                task.onCancelled(result, error);
                runningRequests.remove(this);
                task.onAfterCompleteOrCancelled();
            }

            @Override
            protected void onPostExecute(Result result) {
                task.onComplete(result, error);
                runningRequests.remove(this);
                task.onAfterCompleteOrCancelled();
            }
        };

        executeAsyncTask(asyncTask);

        return asyncTask;
    }

    public AsyncRequestExecutorManager(Executor executor) {
        this.executor = executor;
    }

    public AsyncRequestExecutorManager() {
    }

    private <Result> void executeAsyncTask(final AsyncTask<Void, Void, Result> asyncTask) {
        runningRequests.add(asyncTask);
        if (executor == null) {
            asyncTask.execute();
        } else {
            asyncTask.executeOnExecutor(executor);
        }
    }

    @Override
    public void cancelAll() {
        Tasks.cancelAndClearAsyncTasksQueue(runningRequests);
    }

    @Override
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

    @Override
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
