package com.utilsframework.android.view.pieces;

import android.content.Context;
import android.util.AttributeSet;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 14:52
 */
public class ColorPickerLinearLayout extends StaticPieceProgressView {
    private int[] colors = new int[0];
    private ColorPickerPieceAdapter colorPickerFragmentsAdapter;

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
    public void setAdapter(PieceAdapter pieceAdapter) {
        throw new UnsupportedOperationException("fragments adapter should be an instance " +
                "of ColorPickerPieceAdapter");
    }

    public void setAdapter(ColorPickerPieceAdapter colorPickerFragmentsAdapter) {
        super.setAdapter(colorPickerFragmentsAdapter);
        this.colorPickerFragmentsAdapter = colorPickerFragmentsAdapter;

        if(colorPickerFragmentsAdapter != null){
            colorPickerFragmentsAdapter.setColors(colors);
        }
    }

    @Override
    public void setPiecesProvider(PiecesProvider piecesProvider) {
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
