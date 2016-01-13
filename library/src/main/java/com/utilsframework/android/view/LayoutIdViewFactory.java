package com.utilsframework.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created with IntelliJ IDEA.
 * User: Tikhonenko.S
 * Date: 13.11.13
 * Time: 18:53
 * To change this template use File | Settings | File Templates.
 */
public class LayoutIdViewFactory implements ViewFactory{
    private int layoutId;
    private LayoutInflater layoutInflater;

    public LayoutIdViewFactory(int layoutId, Context context) {
        this.layoutId = layoutId;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View createView() {
        return layoutInflater.inflate(layoutId, null);
    }
}
