package com.dbbest.android.events;

/**
 * User: Tikhonenko.S
 * Date: 27.11.13
 * Time: 16:05
 */
public abstract class DoubleActionHandler {
    private long maxDoubleActionDuration = 2000;
    private long lastActionTime = 0;

    protected DoubleActionHandler(long maxDoubleActionDuration) {
        this.maxDoubleActionDuration = maxDoubleActionDuration;
    }

    protected DoubleActionHandler() {
    }

    public long getMaxDoubleActionDuration() {
        return maxDoubleActionDuration;
    }

    public void setMaxDoubleActionDuration(long maxDoubleActionDuration) {
        if(maxDoubleActionDuration < 0){
            throw new UnsupportedOperationException();
        }

        this.maxDoubleActionDuration = maxDoubleActionDuration;
    }

    public void doAction(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastActionTime < maxDoubleActionDuration){
            onDoubleAction();
            lastActionTime = 0;
        } else {
            onSingleAction();
            lastActionTime = currentTime;
        }
    }

    protected abstract void onSingleAction();
    protected abstract void onDoubleAction();
}
