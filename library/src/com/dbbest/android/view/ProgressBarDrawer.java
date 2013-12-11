package com.dbbest.android.view;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * User: Tikhonenko.S
 * Date: 10.12.13
 * Time: 18:19
 */
public class ProgressBarDrawer implements Drawer{
    private RectF placement;

    public ProgressBarDrawer() {

    }

    public ProgressBarDrawer(RectF placement) {
        this.placement = placement;
    }

    public RectF getPlacement() {
        return placement;
    }

    public void setPlacement(RectF placement) {
        this.placement = placement;
    }

    @Override
    public void onDraw(Canvas canvas) {
        //canvas.draw
    }
}
