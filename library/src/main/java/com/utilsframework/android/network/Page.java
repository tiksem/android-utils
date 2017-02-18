package com.utilsframework.android.network;

import java.util.List;

/**
 * Created by stikhonenko on 2/18/17.
 */
public class Page<T> {
    public Page(List<T> items, boolean isLastPage) {
        this.items = items;
        this.isLastPage = isLastPage;
    }

    public List<T> items;
    public boolean isLastPage;
}
