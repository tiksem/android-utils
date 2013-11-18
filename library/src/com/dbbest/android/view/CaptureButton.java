package com.dbbest.android.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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

    private OnPressStateListener onPressStateListener;

    private Runnable actionDown = new Runnable() {
        @Override
        public void run() {
            actionDownExecuted = true;
            downActionTime = System.currentTimeMillis();
            if(onPressStateListener != null){
                onPressStateListener.onButtonDown();
            }
        }
    };

    private Runnable actionUp = new Runnable() {
        @Override
        public void run() {
            touchDown = false;
            if (onPressStateListener != null) {
                onPressStateListener.onButtonUp();
            }
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int type = motionEvent.getAction();
        switch (type) {
            case MotionEvent.ACTION_DOWN:
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

}