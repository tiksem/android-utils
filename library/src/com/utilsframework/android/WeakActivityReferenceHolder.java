package com.utilsframework.android;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * Created by CM on 9/18/2015.
 */
public class WeakActivityReferenceHolder<T extends Activity> {
    private WeakReference<T> weakReference;

    public WeakActivityReferenceHolder(T activity) {
        weakReference = new WeakReference<T>(activity);
    }

    public T getActivity() {
        return weakReference.get();
    }
}
