package com.dbbest.android.patterns;

import android.app.Activity;
import android.os.Bundle;

import java.util.Stack;

/**
 * User: Tikhonenko.S
 * Date: 08.04.14
 * Time: 13:33
 */
public class ActivityStateManager implements StateChanger{
    private Activity activity;
    private ActivityState state;
    private Stack<ActivityState> backStack = new Stack<ActivityState>();

    public ActivityStateManager(Activity activity, ActivityState state) {
        this.activity = activity;
        this.state = state;

        if(activity == null || state ==  null){
            throw new NullPointerException();
        }
    }

    public void onCreate(Bundle savedInstanceState){
        state.onActivityCreate(activity, savedInstanceState);
        state.onStateCreate(activity, this);
    }

    public void onPause() {
        state.onActivityPause(activity, this);
    }

    public void onStart() {
        state.onActivityStart(activity, this);
    }

    public void onResume() {
        state.onActivityResume(activity, this);
    }

    public void onStop() {
        state.onActivityStop(activity, this);
    }

    public void onDestroy() {
        state.onActivityDestroy(activity);
    }

    public boolean onBackPressed() {
        if(backStack.empty()){
            return false;
        } else {
            ActivityState newState = backStack.pop();
            state.onMoveToAnotherState(activity, newState, this);
            state = newState;
            state.onStateCreate(activity, this);
            return true;
        }
    }

    @Override
    public void changeState(ActivityState newState) {
        state.onMoveToAnotherState(activity, newState, this);
        newState.onStateCreate(activity, this);
        backStack.push(state);
        state = newState;
    }
}
