package com.utilsframework.android.adapters;

import java.util.List;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 12/7/12
 * Time: 6:52 PM
 */
public interface OnNullElementReceived<Element> {
    void onNull(ViewArrayAdapter adapter, List<Element> from, int position);
}
