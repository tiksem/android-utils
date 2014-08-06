package com.utilsframework.android.view.fragments;

import android.view.View;

import java.util.List;

/**
* User: Tikhonenko.S
* Date: 20.11.13
* Time: 13:52
*/
public interface FragmentsAdapter {
    View getView(int index, double sizeInPercents, int size);
    List<Float> getFragments();
}
