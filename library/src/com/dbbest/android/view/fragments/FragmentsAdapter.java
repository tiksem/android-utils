package com.dbbest.android.view.fragments;

import android.view.View;

import java.util.List;

/**
* User: Tikhonenko.S
* Date: 20.11.13
* Time: 13:52
*/
public interface FragmentsAdapter {
    View getView(int index, double sizeInPercents);
    List<Float> getFragments();
}
