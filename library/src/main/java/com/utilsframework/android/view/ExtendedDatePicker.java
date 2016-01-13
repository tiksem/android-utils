package com.utilsframework.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import com.utils.framework.Reflection;

import java.lang.reflect.Field;

/**
 * Created by CM on 6/17/2015.
 */
public class ExtendedDatePicker extends DatePicker {
    private Field mShortMonthsField;
    private Field mMonthSpinnerField;

    private void init() {
        mShortMonthsField = Reflection.getFieldByNameOrThrow(DatePicker.class, "mShortMonths");
        mMonthSpinnerField = Reflection.getFieldByNameOrThrow(DatePicker.class, "mMonthSpinner");
        mShortMonthsField.setAccessible(true);
        mMonthSpinnerField.setAccessible(true);

        Field[] fields = DatePicker.class.getDeclaredFields();
        String[] s = new String[]
                {"January","February","March","April","May","June","July","August","September","October","November",
                        "December"};
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (TextUtils.equals(field.getName(), "mMonthSpinner")) {
                    NumberPicker monthPicker = (NumberPicker) field.get(this);
                    monthPicker.setMinValue(0);
                    monthPicker.setMaxValue(11);
                    monthPicker.setDisplayedValues(s);
                }
                if (TextUtils.equals(field.getName(), "mShortMonths")) {
                    field.set(this, s);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ExtendedDatePicker(Context context) {
        super(context);
    }

    public ExtendedDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendedDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExtendedDatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
