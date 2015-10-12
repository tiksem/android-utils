package com.utilsframework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import com.utilsframework.android.Pauseable;
import com.utilsframework.android.UiLoopEvent;

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
            return;
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        super.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateCheckedState();
            }
        });

        enabledStateUpdater = new UiLoopEvent();
        enabledStateUpdater.run(new Runnable() {
            @Override
            public void run() {
                updateCanPauseState();
            }
        });

        updateCanPauseState();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        enabledStateUpdater.stop();
    }

    public PausedStateToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PausedStateToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PausedStateToggleButton(Context context) {
        super(context);
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    protected void onCanPauseStateChanged(boolean value){
        //setVisibility(value ? View.VISIBLE : View.GONE);
    }
}
