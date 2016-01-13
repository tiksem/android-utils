package com.utilsframework.android.view;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import com.utils.framework.Destroyable;

/**
 * Created by CM on 7/15/2015.
 */
public abstract class KeyboardIsShownListener implements Destroyable {
    private final View contentView;
    private final ViewTreeObserver.OnGlobalLayoutListener listener;
    private final Rect r = new Rect();

    private final int DefaultKeyboardDP = 100;
    private final int EstimatedKeyboardDP = DefaultKeyboardDP +
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);

    public KeyboardIsShownListener(Activity activity) {
        contentView = activity.getWindow().getDecorView();
        listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int estimatedKeyboardHeight = (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP,
                                contentView.getResources().getDisplayMetrics());

                // Conclude whether the keyboard is shown or not.
                contentView.getWindowVisibleDisplayFrame(r);
                int heightDiff = contentView.getRootView().getHeight() - (r.bottom - r.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;
                onKeyboardStateChanged(isShown);
            }
        };
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    protected abstract void onKeyboardStateChanged(boolean isShown);

    @Override
    public void destroy() {
        GuiUtilities.removeGlobalOnLayoutListener(contentView, listener);
    }
}
