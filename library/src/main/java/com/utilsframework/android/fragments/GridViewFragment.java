package com.utilsframework.android.fragments;

import com.utilsframework.android.R;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class GridViewFragment<T> extends ListViewFragment<T> {
    @Override
    protected int getRootLayoutId() {
        return R.layout.grid;
    }
}
