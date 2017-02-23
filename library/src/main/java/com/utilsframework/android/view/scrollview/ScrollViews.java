package com.utilsframework.android.view.scrollview;

import android.view.View;
import android.widget.ScrollView;

/**
 * Created by stikhonenko on 2/23/17.
 */
public class ScrollViews {
    public static void scrollToView(final ScrollView scrollView, final View view) {
        // wait while view measured, e.t.c.
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, view.getBottom());
            }
        }, 30);
    }
}
