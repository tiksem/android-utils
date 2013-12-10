package com.dbbest.android;

import android.content.Context;

/**
 * User: Tikhonenko.S
 * Date: 10.12.13
 * Time: 12:45
 */
public class UiCountableLoopEvent extends UiLoopEvent{
    private int count;
    private int i = 0;

    public UiCountableLoopEvent(Context context, long delay, int count) {
        super(context, delay);
        this.count = count;
    }

    public UiCountableLoopEvent(Context context, int count) {
        super(context);
        this.count = count;
    }

    public int getI() {
        return i;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void run(final Runnable runnable) {
        super.run(new Runnable() {
            @Override
            public void run() {
                if(i < count){
                    runnable.run();
                    i++;
                } else {
                    stop();
                }
            }
        });
    }
}
