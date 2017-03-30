package com.utilsframework.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import com.utilsframework.android.navdrawer.BackPressedListener;

public class ExtendedEditText extends EditText {
    private BackPressedListener backPressedListener;

    public ExtendedEditText(Context context) {
        super(context);
    }

    public ExtendedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExtendedEditText(Context context, AttributeSet attrs,
                            int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BackPressedListener getBackPressedListener() {
        return backPressedListener;
    }

    public void setBackPressedListener(BackPressedListener backPressedListener) {
        this.backPressedListener = backPressedListener;
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (backPressedListener != null) {
            if (keyCode == KeyEvent.KEYCODE_BACK &&
                    event.getAction() == KeyEvent.ACTION_UP) {
                return backPressedListener.shouldOverrideDefaultBackPressedBehavior();
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
