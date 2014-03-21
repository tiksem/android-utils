package com.dbbest.android.patterns;

/**
 * User: Tikhonenko.S
 * Date: 19.03.14
 * Time: 18:48
 */
public interface State {
    int getId();
    Integer getNextState();
    void onStart(State previousState);
    void onLeave();
    boolean shouldMoveToAnotherState();
}
