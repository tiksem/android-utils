package com.utilsframework.android.threading;

public abstract class AbstractCancelable implements Cancelable {
    private boolean isCancelled;

    @Override
    public final boolean cancel(boolean mayInterruptIfRunning) {
        if (!isCancelled) {
            doCancel(mayInterruptIfRunning);
        }
        isCancelled = true;
        return true;
    }

    protected abstract void doCancel(boolean mayInterruptIfRunning);

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }
}
