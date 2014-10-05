package com.utilsframework.android.threading;

import android.os.AsyncTask;

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
    public static void runOnBackground(final ThrowingRunnable action, final OnFinish<Throwable> onFinish){
        new AsyncTask<Void, Void, Throwable>(){
            @Override
            protected Throwable doInBackground(Void... params) {
                try {
                    action.run();
                } catch (Throwable e) {
                    return e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Throwable throwable) {
                if(onFinish != null){
                    onFinish.onFinish(throwable);
                } else if(throwable != null) {
                    throw new RuntimeException(throwable);
                }
            }
        }.execute();
    }

    public static void runOnBackground(final ThrowingRunnable action){
        runOnBackground(action, null);
    }

    public static void runOnBackground(final Runnable runnable, final OnComplete onFinish) {
        new AsyncTask<Void, Void, Void>(){
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
        }.execute();
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
                if(lhs instanceof HighPriorityRunnable){
                    if(rhs instanceof HighPriorityRunnable){
                        return 0;
                    } else {
                        return -1;
                    }
                } else {
                    if(rhs instanceof HighPriorityRunnable){
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        });
    }
}
