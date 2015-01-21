package com.utilsframework.android.resources;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by CM on 1/21/2015.
 */
public class StringUtilities {
    public static void setFormatText(TextView textView, int stringId, Object... args) {
        String string = getFormatString(textView.getContext(), stringId, args);
        textView.setText(string);
    }

    public static String getFormatString(Context context, int stringId, Object... args) {
        String string = context.getString(stringId);
        string = String.format(string, args);
        return string;
    }
}
