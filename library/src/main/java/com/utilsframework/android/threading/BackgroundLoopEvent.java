package com.utilsframework.android.threading;

import android.os.Handler;
import com.utilsframework.android.LoopEvent;

import java.util.concurrent.*;

/**
 * User: Tikhonenko.S
 * Date: 24.12.13
 * Time: 17:14
 */
public class BackgroundLoopEvent implements LoopEvent{
    private long delay = 30;
    private volatile boolean isPaused = false;
    private static ExecutorService executor;
    private Future<?> task;
    private OnStop onStop;
    private Runnable internalOnStop;
    private long maxRunningTime = Long.MAX_VALUE;
    private long startTime;
    private static Handler handler = new Handler();

    public static interface OnStop{
        void onStop(boolean timeIsUp);
    }

    public BackgroundLoopEvent(long delay) {
        this();
        this.delay = delay;
    }

    public BackgroundLoopEvent() {
        if(executor == null){
            executor = new ThreadPoolExecutor(1, 1000, 5000, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }
    }

    public OnStop getOnStop() {
        return onStop;
    }

    public void setOnStop(OnStop onStop) {
        this.onStop = onStop;
    }

    private void onStop(final boolean timeIsUp){
        if(internalOnStop != null){
            handler.post(internalOnStop);
        } else if(onStop != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onStop.onStop(timeIsUp);
                }
            });
        }
    }

    private void doRun(final Runnable runnable){
        startTime = System.currentTimeMillis();
        task = executor.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(!isRunning()){
                        onStop(false);
                        return;
                    }

                    long currentTime = System.currentTimeMillis();

                    if(currentTime - startTime >= maxRunningTime){
                        onStop(true);
                        return;
                    }

                    if (!isPaused) {
                        runnable.run();
                    }

                    if(!isRunning()){
                        onStop(false);
                        return;
                    }

                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        onStop(false);
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void run(final Runnable runnable) {
        run(runnable, false);
    }

    public void run(final Runnable runnable, final boolean executeOnStopOfCurrentlyRunningTask) {
        if (!isRunning()) {
            doRun(runnable);
        } else {
            internalOnStop = new Runnable() {
                @Override
                public void run() {
                    if(executeOnStopOfCurrentlyRunningTask && onStop != null){
                        onStop.onStop(false);
                    }

                    doRun(runnable);
                    internalOnStop = null;
                }
            };
            stop();
        }
    }

    @Override
    public void stop() {
        if(task != null){
            task.cancel(true);
            task = null;
        }
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }

    @Override
    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public boolean isRunning() {
        return task != null;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    @Override
    public void setDelay(long delay) {
        this.delay = delay;
    }

    public long getMaxRunningTime() {
        return maxRunningTime;
    }

    public void setMaxRunningTime(long maxRunningTime) {
        this.maxRunningTime = maxRunningTime;
    }
}
