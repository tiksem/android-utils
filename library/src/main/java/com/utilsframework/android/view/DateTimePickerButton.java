package com.utilsframework.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.utils.framework.strings.Strings;
import com.utilsframework.android.R;
import com.utilsframework.android.time.TimeUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by CM on 6/20/2015.
 */
public class DateTimePickerButton extends TextView {
    private Alerts.DateTimePickerSettings settings = new Alerts.DateTimePickerSettings();
    private Integer dateTimeTextColor;
    private String delimiter;

    private abstract class DateTimeSpan extends ClickableSpan {
        @Override
        public void updateDrawState(TextPaint ds) {
            if (dateTimeTextColor != null) {
                ds.setColor(dateTimeTextColor);
            }
        }
    }

    private void updateText(GregorianCalendar calendar) {
        String dateText = createDateText(calendar);
        String delimiter = createDelimiter();
        String timeText = createTimeText(calendar);

        SpannableString text = new SpannableString(dateText + delimiter + timeText);
        int start = dateText.length();
        text.setSpan(new DateTimeSpan() {
            @Override
            public void onClick(View widget) {
                Alerts.showDatePickerAlert(getContext(), settings);
            }
        }, 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = start + delimiter.length();
        text.setSpan(new DateTimeSpan() {
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
        String month = TimeUtils.getShortMonthName(getContext(), calendar.get(Calendar.MONTH));
        String day = TimeUtils.getFormatDayOrTime(calendar.get(Calendar.DAY_OF_MONTH));
        int year = calendar.get(Calendar.YEAR);

        return day + " " + month + " " + year;
    }

    protected String createTimeText(GregorianCalendar calendar) {
        String hours = TimeUtils.getFormatDayOrTime(calendar.get(Calendar.HOUR_OF_DAY));
        String minutes = TimeUtils.getFormatDayOrTime(calendar.get(Calendar.MINUTE));

        return hours + ":" + minutes;
    }

    protected String createDelimiter() {
        String delimiter = this.delimiter;
        if (delimiter == null) {
            delimiter = getContext().getString(R.string.date_time_divider);
        }

        return Strings.quote(delimiter, " ");
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray a = context.obtainStyledAttributes(attributeSet,
                    R.styleable.DateTimePickerButton);
            try {
                if (a.hasValue(R.styleable.DateTimePickerButton_dateTimeTextColor)) {
                    this.dateTimeTextColor = a.getColor(
                            R.styleable.DateTimePickerButton_dateTimeTextColor, 0);
                } else {
                    this.dateTimeTextColor = null;
                }
                delimiter = a.getString(R.styleable.DateTimePickerButton_delimiter);
            } finally {
                a.recycle();
            }
        }

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
        init(context, null);
    }

    public DateTimePickerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateTimePickerButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public DateTimePickerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public long getDate() {
        return settings.currentTimeInMillis;
    }

    public void setDate(long dateMillis) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dateMillis);
        updateText(calendar);
    }
}
