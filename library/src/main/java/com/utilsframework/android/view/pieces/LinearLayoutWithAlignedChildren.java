package com.utilsframework.android.view.pieces;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 14:47
 */
public class LinearLayoutWithAlignedChildren extends StaticPieceProgressView {
    public LinearLayoutWithAlignedChildren(Context context) {
        super(context);
        init();
    }

    public LinearLayoutWithAlignedChildren(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearLayoutWithAlignedChildren(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setAdapter(PieceAdapter pieceAdapter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPiecesProvider(PiecesProvider piecesProvider) {
        throw new UnsupportedOperationException();
    }

    private void init() {
        super.setPiecesProvider(new PiecesProvider() {
            @Override
            public float getFragment(int viewIndex, View view) {
                return 1;
            }
        });
    }
}
