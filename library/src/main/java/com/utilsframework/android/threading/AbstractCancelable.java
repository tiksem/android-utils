package com.utilsframework.android.threading;

public abstract class AbstractCancelable implements Cancelable {
    private boolean isCancelled;

    @Override
    public final void cancel(boolean mayInterruptIfRunning) {
        if (!isCancelled) {
            doCancel(mayInterruptIfRunning);
        }
        isCancelled = true;
    }

    protected abstract void doCancel(boolean mayInterruptIfRunning);

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }
}
