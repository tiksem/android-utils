package com.utilsframework.android.view.fragments;

import java.util.Collections;
import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 15:03
 */
public abstract class ColorPickerFragmentsAdapter implements FragmentsAdapter{
    private int[] colors = new int[0];

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
