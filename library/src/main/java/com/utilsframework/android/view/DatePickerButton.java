package com.utilsframework.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.utilsframework.android.time.TimeUtils;

import java.util.GregorianCalendar;

/**
 * Created by CM on 6/20/2015.
 */
public class DatePickerButton extends TextView {
    private Alerts.DateTimePickerSettings settings = new Alerts.DateTimePickerSettings();
    private OnDateChangedListener onDateChangedListener;

    public interface OnDateChangedListener {
        void onDateChanged();
    }

    private void updateText(GregorianCalendar calendar) {
        String displayDate = TimeUtils.getAlternativeDisplayDate(getContext(), calendar);
        setText(displayDate);
        long timeInMillis = calendar.getTimeInMillis();
        long currentTimeInMillis = settings.currentTimeInMillis;
        settings.currentTimeInMillis = timeInMillis;

        if (timeInMillis != currentTimeInMillis && onDateChangedListener != null) {
            onDateChangedListener.onDateChanged();
        }
    }

    private void init() {
        settings.currentTimeInMillis = System.currentTimeMillis();
        settings.onDateTimeSelected = new Alerts.OnDateTimeSelected() {
            @Override
            public void onSelected(GregorianCalendar calendar) {
                updateText(calendar);
            }
        };

        settings.clearTime = true;

        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Alerts.showDatePickerAlert(getContext(), settings);
            }
        });

        updateText(new GregorianCalendar());
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        throw new UnsupportedOperationException();
    }

    public DatePickerButton(Context context) {
        super(context);
        init();
    }

    public DatePickerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DatePickerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DatePickerButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public long getDate() {
        return settings.currentTimeInMillis;
    }

    public OnDateChangedListener getOnDateChangedListener() {
        return onDateChangedListener;
    }

    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener) {
        this.onDateChangedListener = onDateChangedListener;
    }
}
