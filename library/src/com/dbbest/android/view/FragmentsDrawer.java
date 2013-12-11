package com.dbbest.android.view;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * User: Tikhonenko.S
 * Date: 10.12.13
 * Time: 18:17
 */
public class FragmentsDrawer implements Drawer{
    public static enum Mode{
        DRAW_PROGRESS_BAR,
        DRAW_FRAGMENTS_ONLY
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
    }
}
