package com.dbbest.android.view.fragments;

import android.view.View;

import java.util.Collections;
import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 15:03
 */
public abstract class ColorPickerFragmentsAdapter implements FragmentsAdapter{
    private int[] colors;

    void setColors(int[] colors) {
        this.colors = colors;
    }

    public int getColor(int index) {
        return colors[index];
    }

    @Override
    public List<Float> getFragments() {
        return Collections.nCopies(colors.length, 1.0f);
    }
}
