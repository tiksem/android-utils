package com.dbbest.android.view.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 14:52
 */
public class ColorPickerLinearLayout extends StaticFragmentsView{
    private int[] colors = new int[0];
    private ColorPickerFragmentsAdapter colorPickerFragmentsAdapter;

    public ColorPickerLinearLayout(Context context) {
        super(context);
    }

    public ColorPickerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPickerLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(FragmentsAdapter fragmentsAdapter) {
        throw new UnsupportedOperationException();
    }

    public void setAdapter(ColorPickerFragmentsAdapter colorPickerFragmentsAdapter) {
        super.setAdapter(colorPickerFragmentsAdapter);
        this.colorPickerFragmentsAdapter = colorPickerFragmentsAdapter;

        if(colorPickerFragmentsAdapter != null){
            colorPickerFragmentsAdapter.setColors(colors);
        }
    }

    @Override
    public void setFragmentsProvider(FragmentsProvider fragmentsProvider) {
        throw new UnsupportedOperationException();
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(final int... colors) {
        if(colors == null){
            throw new NullPointerException();
        }

        this.colors = colors;

        if(colorPickerFragmentsAdapter != null){
            colorPickerFragmentsAdapter.setColors(colors);
        }

        updateFragments();
    }
}
