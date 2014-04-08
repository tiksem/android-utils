package com.dbbest.android.patterns;

import android.app.Activity;
import android.os.Bundle;

/**
 * User: Tikhonenko.S
 * Date: 08.04.14
 * Time: 13:30
 */
public interface ActivityState {
    void onActivityCreate(Activity activity, Bundle savedInstanceState);
    void onStateCreate(Activity activity, StateChanger stateChanger);
    void onMoveToAnotherState(Activity activity, ActivityState previousState, StateChanger stateChanger);
    void onActivityDestroy(Activity activity);
    void onActivityStart(Activity activity, StateChanger stateChanger);
    void onActivityStop(Activity activity, StateChanger stateChanger);
    void onActivityResume(Activity activity, StateChanger stateChanger);
    void onActivityPause(Activity activity, StateChanger stateChanger);
}
