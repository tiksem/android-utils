package com.dbbest.android.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;
import android.widget.ImageButton;

/**
 * Created with IntelliJ IDEA.
 * User: malets.d
 * Date: 9/6/13
 * Time: 11:30 AM
 */
public class CaptureButton extends ImageButton implements View.OnTouchListener, UpDownButton {
    private static final String TAG = CaptureButton.class.getSimpleName();

    private long downActionDelay = 0;
    private long minUpActionDelay = 0;
    private boolean touchDown = false;
    private boolean actionDownExecuted = false;
    private long downActionTime;
    private Handler downActionHandler = new Handler();
    private boolean downActionEnabled = true;
    private int lastTouchAction = Integer.MAX_VALUE;

    private OnPressStateListener onPressStateListener;

    private Runnable actionDown = new Runnable() {
        @Override
        public void run() {
            if(lastTouchAction == MotionEvent.ACTION_DOWN){
                return;
            }

            Log.i("CaptureButton","actionDown");

            actionDownExecuted = true;
            downActionTime = System.currentTimeMillis();
            if(onPressStateListener != null){
                onPressStateListener.onButtonDown();
            }
            lastTouchAction = MotionEvent.ACTION_DOWN;
        }
    };

    private Runnable actionUp = new Runnable() {
        @Override
        public void run() {
            if(lastTouchAction == MotionEvent.ACTION_UP){
                return;
            }

            Log.i("CaptureButton","actionUp");

            touchDown = false;
            if (onPressStateListener != null) {
                onPressStateListener.onButtonUp();
            }
            lastTouchAction = MotionEvent.ACTION_UP;
        }
    };

    private void init(){
        setOnTouchListener(this);
    }

    public CaptureButton(Context context) {
        super(context);
        init();
    }

    public CaptureButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CaptureButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public boolean isTouchDown() {
        return touchDown;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int type = motionEvent.getAction();
        switch (type) {
            case MotionEvent.ACTION_DOWN:
                if(!downActionEnabled){
                    return true;
                }

                if(touchDown){
                    return true;
                }

                touchDown = true;
                downActionHandler.postDelayed(actionDown, downActionDelay);
                return true;
            case MotionEvent.ACTION_UP:
                if(!actionDownExecuted){
                    downActionHandler.removeCallbacks(actionDown);
                    touchDown = false;
                } else {
                    long currentTime = System.currentTimeMillis();
                    long timeDif = currentTime - downActionTime;
                    if(timeDif >= minUpActionDelay){
                        post(actionUp);
                    } else {
                        postDelayed(actionUp, minUpActionDelay - timeDif);
                    }
                }

                return true;
        }

        return false;
    }

    public long getMinUpActionDelay() {
        return minUpActionDelay;
    }

    public void setMinUpActionDelay(long minUpActionDelay) {
        this.minUpActionDelay = minUpActionDelay;
    }

    public long getDownActionDelay() {
        return downActionDelay;
    }

    public void setDownActionDelay(long downActionDelay) {
        this.downActionDelay = downActionDelay;
    }

    @Override
    public OnPressStateListener getOnPressStateListener() {
        return onPressStateListener;
    }

    @Override
    public void setOnPressStateListener(OnPressStateListener onPressStateListener) {
        this.onPressStateListener = onPressStateListener;
    }

    public boolean isDownActionEnabled() {
        return downActionEnabled;
    }

    public void setDownActionEnabled(boolean downActionEnabled) {
        this.downActionEnabled = downActionEnabled;
    }
}