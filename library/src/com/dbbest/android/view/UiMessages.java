package com.dbbest.android.view;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Tikhonenko.S on 20.09.13.
 */
public class UiMessages {
    public static void error(Context context, CharSequence text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void error(Context context, int stringResourceId){
        String message = context.getResources().getString(stringResourceId);
        error(context, message);
    }

    public static void message(Context context, CharSequence text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void message(Context context, int stringResourceId){
        String message = context.getResources().getString(stringResourceId);
        error(context, message);
    }
}
