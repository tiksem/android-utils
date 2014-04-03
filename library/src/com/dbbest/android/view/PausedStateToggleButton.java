package com.dbbest.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import com.dbbest.android.Pauseable;
import com.dbbest.android.UiLoopEvent;

/**
 * User: Tikhonenko.S
 * Date: 31.03.14
 * Time: 14:44
 */
public class PausedStateToggleButton extends ToggleButton{
    private Pauseable pauseable;
    private UiLoopEvent enabledStateUpdater;
    private boolean lastCanPauseState = false;

    public Pauseable getPauseable() {
        return pauseable;
    }

    public void setPauseable(Pauseable pauseable) {
        this.pauseable = pauseable;
        updateCheckedState();
    }

    private void updateCheckedState(){
        if(pauseable == null){
            throw new IllegalStateException("Broken logic. You can not toggle checked state while pausable is null");
        }

        if(isChecked()){
            pauseable.pause();
        } else {
            pauseable.resume();
        }
    }

    private void updateCanPauseState(){
        boolean canPauseState = pauseable != null && pauseable.canPause();
        if(lastCanPauseState == canPauseState){
            return;
        }

        if(canPauseState){
            onCanPauseStateChanged(true);
            updateCheckedState();
        } else {
            onCanPauseStateChanged(false);
        }

        lastCanPauseState = canPauseState;
    }

    private void init(){
        super.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateCheckedState();
            }
        });

        enabledStateUpdater = new UiLoopEvent(getContext());
        enabledStateUpdater.run(new Runnable() {
            @Override
            public void run() {
                updateCanPauseState();
            }
        });

        updateCanPauseState();
    }

    public PausedStateToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public PausedStateToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PausedStateToggleButton(Context context) {
        super(context);
        init();
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    protected void onCanPauseStateChanged(boolean value){
        setVisibility(value ? View.VISIBLE : View.GONE);
    }
}