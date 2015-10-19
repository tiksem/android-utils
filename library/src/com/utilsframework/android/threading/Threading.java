package com.utilsframework.android.threading;

import android.os.AsyncTask;
import android.os.Handler;

import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;

/**
 * User: Tikhonenko.S
 * Date: 22.11.13
 * Time: 17:26
 */
public final class Threading {
    private static final Handler HANDLER = new Handler();

    public static <ErrorType extends Throwable> void runOnBackground(final ThrowingRunnable<ErrorType> action,
                                                                     final OnFinish<ErrorType> onFinish,
                                                                     final Class<ErrorType> errorClass) {
        new AsyncTask<Void, Void, ErrorType>() {
            @Override
            protected ErrorType doInBackground(Void... params) {
                try {
                    action.run();
                } catch (Throwable e) {
                    if (errorClass.isAssignableFrom(e.getClass())) {
                        return (ErrorType) e;
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(ErrorType error) {
                if (onFinish != null) {
                    onFinish.onFinish(error);
                } else if (error != null) {
                    throw new RuntimeException(error);
                }
            }
        }.execute();
    }

    public static AsyncTask runOnBackground(final Runnable runnable, final OnComplete onFinish) {
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                runnable.run();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (onFinish != null) {
                    onFinish.onFinish();
                }
            }
        };
        asyncTask.execute();
        return asyncTask;
    }

    public static void runOnBackground(final Runnable runnable) {
        runOnBackground(runnable, null);
    }

    public static ThreadFactory lowPriorityThreadFactory() {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setPriority(Thread.MIN_PRIORITY);
                return thread;
            }
        };
    }

    public static BlockingQueue<Runnable> highPriorityBlockingQueue() {
        return new PriorityBlockingQueue<Runnable>(11, new Comparator<Runnable>() {
            @Override
            public int compare(Runnable lhs, Runnable rhs) {
                if (lhs instanceof HighPriorityRunnable) {
                    if (rhs instanceof HighPriorityRunnable) {
                        return 0;
                    } else {
                        return -1;
                    }
                } else {
                    if (rhs instanceof HighPriorityRunnable) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        });
    }

    public interface ResultProvider<T> {
        T get();
    }

    public static <T> void getResultAsync(final ResultProvider<T> resultProvider, final OnFinish<T> onFinish) {
        new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {
                return resultProvider.get();
            }

            @Override
            protected void onPostExecute(T result) {
                onFinish.onFinish(result);
            }
        }.execute();
    }

    public static abstract class Task<ErrorType extends Throwable, Result> {
        public abstract Result runOnBackground() throws ErrorType;
        public abstract void onComplete(Result result, ErrorType error);
        public void onCancelled(Result result, ErrorType error) {}
        public void onAfterCompleteOrCancelled() {}
    }

    public static <Result> AsyncTask executeNetworkRequest(final Task<IOException, Result> task) {
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
                task.onAfterCompleteOrCancelled();
            }

            @Override
            protected void onPostExecute(Result result) {
                task.onComplete(result, error);
                task.onAfterCompleteOrCancelled();
            }
        };

        asyncTask.execute();
        return asyncTask;
    }

    public static <ErrorType extends Throwable, Result> void executeAsyncTask(final Task<ErrorType, Result> task,
                                                                              final Class<ErrorType> errorClass) {
        new AsyncTask<Void, Void, Result>(){
            ErrorType errorType;

            @Override
            protected Result doInBackground(Void... params) {
                Result result = null;
                try {
                    result = task.runOnBackground();
                } catch (Throwable error) {
                    if (errorClass.isAssignableFrom(error.getClass())) {
                        errorType = (ErrorType) error;
                    } else if(error instanceof RuntimeException) {
                        throw (RuntimeException)error;
                    } else {
                        throw (Error)error;
                    }
                }

                return result;
            }

            @Override
            protected void onPostExecute(Result result) {
                task.onComplete(result, errorType);
            }
        }.execute();
    }
}
