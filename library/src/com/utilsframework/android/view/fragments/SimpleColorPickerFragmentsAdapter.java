package com.utilsframework.android.view.fragments;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 15:09
 */
public class SimpleColorPickerFragmentsAdapter extends ColorPickerFragmentsAdapter{
    private Context context;

    public SimpleColorPickerFragmentsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getView(int index, double sizeInPercents) {
        int color = getColor(index);
        TextView result = new TextView(context);
        result.setBackgroundColor(color);
        return result;
    }
}
