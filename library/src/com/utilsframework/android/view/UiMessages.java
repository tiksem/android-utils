package com.utilsframework.android.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.utilsframework.android.R;

/**
 * Created by Tikhonenko.S on 20.09.13.
 */
public class UiMessages {
    public static void error(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void error(Context context, int stringResourceId) {
        String message = context.getResources().getString(stringResourceId);
        error(context, message);
    }

    public static void error(Context context, CharSequence text, Object... args) {
        text = String.format(text.toString(), args);
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void error(Context context, int stringResourceId, Object... args) {
        String message = context.getResources().getString(stringResourceId);
        message(context, message, args);
    }

    public static void message(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void message(Context context, CharSequence text, Object... args) {
        text = String.format(text.toString(), args);
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void message(Context context, int stringResourceId, Object... args) {
        String message = context.getResources().getString(stringResourceId);
        message(context, message, args);
    }

    public static void message(Context context, int stringResourceId) {
        String message = context.getResources().getString(stringResourceId);
        error(context, message);
    }

    public static void messageAtCenter(Context context, CharSequence message) {

        View view = LayoutInflater.from(context).inflate(R.layout.centered_toast, null);
        TextView messageView = (TextView) view.findViewById(R.id.toast_text);
        messageView.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
    }
}
