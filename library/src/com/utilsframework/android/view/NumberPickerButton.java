package com.utilsframework.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by CM on 6/19/2015.
 */
public class NumberPickerButton extends Button {
    Alerts.NumberPickerAlertSettings settings = new Alerts.NumberPickerAlertSettings();

    private void init() {
        settings.current = settings.min;
        settings.onNumberSelected = new OnNumberSelected() {
            @Override
            public void onSelected(int value) {
                updateCurrent(value);
            }
        };

        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Alerts.showNumberPickerAlert(getContext(), settings);
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        throw new UnsupportedOperationException();
    }

    public NumberPickerButton(Context context) {
        super(context);
        init();
    }

    public NumberPickerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumberPickerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NumberPickerButton(Context context, AttributeSet attrs,
                              int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public int getMaxValue() {
        return settings.max;
    }

    public void setMaxValue(int maxValue) {
        if (settings.min > maxValue) {
            throw new IllegalArgumentException("minValue > maxValue");
        }

        if (maxValue < settings.current) {
            updateCurrent(maxValue);
        }

        settings.max = maxValue;
    }

    public int getMinValue() {
        return settings.min;
    }

    public void setMinValue(int minValue) {
        if (minValue > settings.max) {
            throw new IllegalArgumentException("minValue > maxValue");
        }

        if (minValue > settings.current) {
            updateCurrent(minValue);
        }

        settings.min = minValue;
    }

    public int getValue() {
        return settings.current;
    }

    public void setValue(int value) {
        if (value > settings.max) {
            value = settings.max;
        } else if(value < settings.min) {
            value = settings.min;
        }

        updateCurrent(value);
    }

    private void updateCurrent(int value) {
        settings.current = value;
        setText(Integer.toString(value));
    }

    public void setPickerDialogMessage(CharSequence message) {
        settings.message = message;
    }

    public void setPickerDialogTitle(CharSequence title) {
        settings.title = title;
    }
}
