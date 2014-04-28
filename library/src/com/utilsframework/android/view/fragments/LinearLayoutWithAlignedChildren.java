package com.utilsframework.android.view.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 14:47
 */
public class LinearLayoutWithAlignedChildren extends StaticFragmentsView{
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
    public void setAdapter(FragmentsAdapter fragmentsAdapter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFragmentsProvider(FragmentsProvider fragmentsProvider) {
        throw new UnsupportedOperationException();
    }

    private void init() {
        super.setFragmentsProvider(new FragmentsProvider() {
            @Override
            public float getFragment(int viewIndex, View view) {
                return 1;
            }
        });
    }
}
