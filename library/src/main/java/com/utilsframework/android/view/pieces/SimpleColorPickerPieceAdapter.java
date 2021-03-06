package com.utilsframework.android.view.pieces;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 15:09
 */
public class SimpleColorPickerPieceAdapter extends ColorPickerPieceAdapter {
    private Context context;

    public SimpleColorPickerPieceAdapter(Context context) {
        this.context = context;
    }

    @Override
    public View getView(int index, double sizeInPercents, int size) {
        int color = getColor(index);
        TextView result = new TextView(context);
        result.setBackgroundColor(color);
        return result;
    }
}
