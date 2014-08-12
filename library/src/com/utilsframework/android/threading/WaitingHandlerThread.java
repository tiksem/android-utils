package com.utilsframework.android.threading;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * User: Tikhonenko.S
 * Date: 12.08.14
 * Time: 19:40
 */
public class WaitingHandlerThread extends Thread {
    private Handler handler;
    private final Object waiter = new Object();

    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler(Looper.myLooper());
        synchronized (waiter) {
            waiter.notifyAll();
        }
        Looper.loop();
    }

    @Override
    public synchronized void start() {
        super.start();
        synchronized (waiter) {
            try {
                waiter.wait();
            } catch (InterruptedException e) {

            }
        }
    }

    public Handler getHandler() {
        return handler;
    }

    /**
     * Shuts everything down.
     */
    public void shutdown() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Looper.myLooper().quit();
                handler = null;
            }
        });
    }
}
