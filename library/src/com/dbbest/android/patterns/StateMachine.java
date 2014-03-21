package com.dbbest.android.patterns;

import android.content.Context;
import com.dbbest.android.RunningLoopEvent;
import com.dbbest.android.UiLoopEvent;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: Tikhonenko.S
 * Date: 19.03.14
 * Time: 18:46
 */
public class StateMachine {
    private LinkedHashMap<Integer, State> states;
    private State currentState;
    private UiLoopEvent stateUpdater;

    public void updateStates(){

    }

    private void onStateMachineWorkFinished(){
        stateUpdater.stop();
    }

    public StateMachine(Context context) {
        stateUpdater = new UiLoopEvent(context);
    }

    public RunningLoopEvent getStateUpdater() {
        return stateUpdater;
    }

    public void start(int stateId){
        if(currentState != null){
            throw new RuntimeException("the StateMachine has been already started");
        }

        setState(stateId);

        stateUpdater.run(new Runnable() {
            @Override
            public void run() {
                if(currentState.shouldMoveToAnotherState()){
                    Integer nextState = currentState.getNextState();
                    if(nextState != null){
                        setState(nextState);
                    } else {
                        onStateMachineWorkFinished();
                    }
                }
            }
        });
    }

    private void setState(int stateId){
        State state = states.get(stateId);
        if(state == null){
            throw new NoSuchStateException(stateId);
        }

        if(currentState != null){
            currentState.onLeave();
        }

        state.onStart(currentState);
        currentState = state;
    }
}
