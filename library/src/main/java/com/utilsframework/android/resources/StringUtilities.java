package com.utilsframework.android.resources;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.TextView;

import java.util.Locale;

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
