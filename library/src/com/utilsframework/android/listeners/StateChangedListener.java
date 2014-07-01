package com.utilsframework.android.listeners;

import com.utils.framework.patterns.StateProvider;
import com.utilsframework.android.UiLoopEvent;

import java.lang.ref.WeakReference;

/**
 * User: Tikhonenko.S
 * Date: 30.04.14
 * Time: 18:24
 */
public abstract class StateChangedListener<T, State> implements StateProvider<T, State> {
    private UiLoopEvent uiLoopEvent;
    private WeakReference<T> object;
    private State lastState;

    public StateChangedListener(T object, Object context) {
        if(context == null){
            context = object;
        }

        uiLoopEvent = new UiLoopEvent(context);
        this.object = new WeakReference<T>(object);
    }

    public StateChangedListener(T object){
        this(object, null);
    }

    public void resume(){
        uiLoopEvent.resume();
    }

    public void pause(){
        uiLoopEvent.pause();
    }

    private void updateStats(T object){
        State state = getState(object);
        if(!stateEquals(state, lastState)){
            onStateChanged(object, lastState, state);
            lastState = cloneState(state);
        }
    }

    private T getObjectOrThrow() {
        T result = object.get();
        if(result == null){
            throw new IllegalStateException("object was deleted");
        }

        return result;
    }

    public void start(){
        uiLoopEvent.run(new Runnable() {
            @Override
            public void run() {
                T objectValue = object.get();
                if(objectValue == null){
                    uiLoopEvent.stop();
                    return;
                }

                updateStats(objectValue);
            }
        });
    }

    public void stop(){
        uiLoopEvent.stop();
    }

    protected abstract void onStateChanged(T object, State lastState, State currentState);
    protected boolean stateEquals(State a, State b){
        return a == null ? b == null : a.equals(b);
    }

    protected State cloneState(State original){
        return original;
    }

    @Override
    public void restoreState(T object, State state) {
        throw new UnsupportedOperationException();
    }
}
