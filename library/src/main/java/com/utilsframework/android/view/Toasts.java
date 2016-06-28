package com.utilsframework.android.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.utilsframework.android.R;
import com.utilsframework.android.UiLoopEvent;

/**
 * Created by Tikhonenko.S on 20.09.13.
 */
public class Toasts {
    public static void toast(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, CharSequence text, Object... args) {
        text = String.format(text.toString(), args);
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, int stringResourceId, Object... args) {
        String message = context.getResources().getString(stringResourceId);
        toast(context, message, args);
    }

    public static void toast(Context context, int stringResourceId) {
        String message = context.getResources().getString(stringResourceId);
        toast(context, message);
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

    public static Toast customView(View view, int length) {
        Toast toast = new Toast(view.getContext());
        toast.setDuration(length);
        toast.setView(view);
        toast.show();
        return toast;
    }

    public static Toast customView(Context context, int layoutId, int length) {
        View view = View.inflate(context, layoutId, null);
        return customView(view, length);
    }

    public static Toast customViewAtCenter(View view, int length) {
        Toast toast = new Toast(view.getContext());
        toast.setDuration(length);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.show();
        return toast;
    }

    public static Toast customViewAtCenter(Context context, int layoutId, int length) {
        View view = View.inflate(context, layoutId, null);
        return customViewAtCenter(view, length);
    }

    // Note: don't forget to cancel the toast in onDestroy/onViewDestroyed e.t.c.
    public static Controller makeInfinite(final Toast toast) {
        final UiLoopEvent uiLoopEvent = new UiLoopEvent(1000);
        uiLoopEvent.run(new Runnable() {
            @Override
            public void run() {
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
            }
        });

        return new Controller() {
            @Override
            public void dismiss() {
                toast.cancel();
                uiLoopEvent.stop();
            }

            @Override
            public void pauseShowing() {
                toast.cancel();
                uiLoopEvent.pause();
            }

            @Override
            public void resumeShowing() {
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
                uiLoopEvent.resume();
            }
        };
    }

    public interface Controller {
        void dismiss();
        void pauseShowing();
        void resumeShowing();
    }

    public static Controller infiniteCustomViewAtCenter(View view) {
        Toast toast = customViewAtCenter(view, Toast.LENGTH_LONG);
        return makeInfinite(toast);
    }

    public static Controller infiniteCustomViewAtCenter(Context context, int layoutId) {
        View view = View.inflate(context, layoutId, null);
        return infiniteCustomViewAtCenter(view);
    }
}
