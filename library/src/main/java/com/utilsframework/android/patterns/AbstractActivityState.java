package com.utilsframework.android.patterns;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.utilsframework.android.view.GuiUtilities;
import com.utilsframework.android.view.ViewsVisibilityToggle;

/**
 * User: Tikhonenko.S
 * Date: 08.04.14
 * Time: 13:38
 */
public abstract class AbstractActivityState<SharedStateData> implements ActivityState{
    private Activity activity;
    private StateChanger stateChanger;
    private SharedStateData sharedStateData;
    private ViewsVisibilityToggle viewsGoneVisibilityToggle;

    public SharedStateData getSharedStateData() {
        return sharedStateData;
    }

    public Activity getActivity() {
        return activity;
    }

    public StateChanger getStateChanger() {
        return stateChanger;
    }

    @Override
    public final void onActivityCreate(Activity activity, Bundle savedInstanceState) {
        this.activity = activity;
        sharedStateData = initSharedStateData(activity);
        if(!sharedStateData.getClass().isAssignableFrom(getSharedStateDataClass())){
            throw new ClassCastException();
        }
    }

    @Override
    public void onStateCreate(Activity activity, StateChanger stateChanger) {
        this.activity = activity;
        this.stateChanger = stateChanger;

        if(viewsGoneVisibilityToggle == null){
            int[] viewsIdesToMakeGone = getViewsToMakeGone();
            if(viewsIdesToMakeGone.length > 0) {
                viewsGoneVisibilityToggle = new ViewsVisibilityToggle(
                        GuiUtilities.resourceIdArrayToViewArray(activity, viewsIdesToMakeGone));
                viewsGoneVisibilityToggle.setVisibility(View.GONE);
            }
        } else {
            viewsGoneVisibilityToggle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMoveToAnotherState(Activity activity, ActivityState nextState, StateChanger stateChanger) {
        if(nextState instanceof AbstractActivityState){
            AbstractActivityState nextAbstractState = (AbstractActivityState)nextState;
            if(nextAbstractState.getSharedStateDataClass().isAssignableFrom(getSharedStateDataClass())){
                nextAbstractState.sharedStateData = sharedStateData;
            }

            if(viewsGoneVisibilityToggle != null){
                viewsGoneVisibilityToggle.restoreVisibility();
            }
        }
    }

    @Override
    public void onActivityDestroy(Activity activity) {
    }

    @Override
    public void onActivityStart(Activity activity, StateChanger stateChanger) {
    }

    @Override
    public void onActivityStop(Activity activity, StateChanger stateChanger) {
    }

    @Override
    public void onActivityResume(Activity activity, StateChanger stateChanger) {
    }

    @Override
    public void onActivityPause(Activity activity, StateChanger stateChanger) {
    }

    protected SharedStateData initSharedStateData(Activity activity){
        return null;
    }

    protected int[] getViewsToMakeGone() {
        return new int[0];
    }

    protected abstract Class<? extends SharedStateData> getSharedStateDataClass();
}
