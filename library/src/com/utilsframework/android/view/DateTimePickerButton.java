package com.utilsframework.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.utilsframework.android.time.TimeUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by CM on 6/20/2015.
 */
public class DateTimePickerButton extends TextView {
    private Alerts.DateTimePickerSettings settings = new Alerts.DateTimePickerSettings();

    private void updateText(GregorianCalendar calendar) {
        String dateText = createDateText(calendar);
        String delimiter = createDelimeter();
        String timeText = createTimeText(calendar);

        SpannableString text = new SpannableString(dateText + delimiter + timeText);
        int start = dateText.length();
        text.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Alerts.showDatePickerAlert(getContext(), settings);
            }
        }, 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = start + delimiter.length();
        text.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Alerts.showTimePickerAlert(getContext(), settings);
            }
        }, start, start + timeText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        settings.currentTimeInMillis = calendar.getTimeInMillis();
        setText(text);

        setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected String createDateText(GregorianCalendar calendar) {
        String month = TimeUtils.getShortMonthName(calendar.get(Calendar.MONTH));
        String day = TimeUtils.getFormatDayOrTime(calendar.get(Calendar.DAY_OF_MONTH));
        int year = calendar.get(Calendar.YEAR);

        return day + " " + month + " " + year;
    }

    protected String createTimeText(GregorianCalendar calendar) {
        String hours = TimeUtils.getFormatDayOrTime(calendar.get(Calendar.HOUR_OF_DAY));
        String minutes = TimeUtils.getFormatDayOrTime(calendar.get(Calendar.MINUTE));

        return hours + ":" + minutes;
    }

    protected String createDelimeter() {
        return " at ";
    }

    private void init() {
        settings.onDateTimeSelected = new Alerts.OnDateTimeSelected() {
            @Override
            public void onSelected(GregorianCalendar calendar) {
                updateText(calendar);
            }
        };

        updateText(new GregorianCalendar());
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        throw new UnsupportedOperationException();
    }

    public DateTimePickerButton(Context context) {
        super(context);
        init();
    }

    public DateTimePickerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateTimePickerButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public DateTimePickerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
}
