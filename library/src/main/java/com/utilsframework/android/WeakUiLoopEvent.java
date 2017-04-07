package com.utilsframework.android;

import java.lang.ref.WeakReference;

/**
 * Created by stykhonenko on 12.10.15.
 */

/* WeakUiLoopEvent stops, when its parent becomes weak-reachable.*/
public class WeakUiLoopEvent<T> extends UiLoopEvent {
    private WeakReference<T> parent;

    public T get() {
        return parent.get();
    }

    public WeakUiLoopEvent(T parent) {
        this(DEFAULT_DELAY, parent);
    }

    public WeakUiLoopEvent(long delay, T parent) {
        super(delay);
        if (parent == null) {
            throw new IllegalArgumentException("parent == null");
        }

        this.parent = new WeakReference<T>(parent);
    }

    @Override
    protected boolean canRun() {
        return parent.get() != null;
    }
}
