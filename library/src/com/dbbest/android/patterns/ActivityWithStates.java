package com.dbbest.android.patterns;

import android.app.Activity;
import android.os.Bundle;

/**
 * User: Tikhonenko.S
 * Date: 08.04.14
 * Time: 13:50
 */
public abstract class ActivityWithStates extends Activity {
    private ActivityStateManager activityStateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStateManager = new ActivityStateManager(this, getFirstState());
        activityStateManager.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityStateManager.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityStateManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityStateManager.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityStateManager.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityStateManager.onDestroy();
    }

    @Override
    public void onBackPressed() {
        activityStateManager.onBackPressed();
    }

    protected abstract ActivityState getFirstState();
}
