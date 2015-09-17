package com.utilsframework.android.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by CM on 9/18/2015.
 */
public class WeakFragmentReferenceHolder<T extends Fragment> {
    private WeakReference<T> weakReference;

    public WeakFragmentReferenceHolder(T fragment) {
        weakReference = new WeakReference<T>(fragment);
    }

    public T getFragment() {
        return weakReference.get();
    }

    public View getView() {
        T fragment = getFragment();
        if (fragment != null) {
            return fragment.getView();
        }

        return null;
    }
}
