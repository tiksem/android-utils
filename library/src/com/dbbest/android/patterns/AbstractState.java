package com.dbbest.android.patterns;

/**
 * User: Tikhonenko.S
 * Date: 19.03.14
 * Time: 18:51
 */
public abstract class AbstractState implements State{
    private boolean shouldMoveToAnotherState = false;

    protected void moveToAnotherState(){
        shouldMoveToAnotherState = true;
    }

    protected void onUpdate(){

    }

    @Override
    public void onLeave() {

    }

    @Override
    public int getId() {
        return System.identityHashCode(this);
    }

    @Override
    public final boolean shouldMoveToAnotherState() {
        return shouldMoveToAnotherState;
    }
}
