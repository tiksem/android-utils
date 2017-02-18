package com.utilsframework.android.adapters.navigation;

import android.content.Context;
import android.view.View;
import com.utilsframework.android.R;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by CM on 6/22/2015.
 */
public abstract class LazyLoadingListAdapter<Element, ViewHolder> extends ViewArrayAdapter<Element, ViewHolder> {
    public LazyLoadingListAdapter(Context context) {
        super(context);
    }

    public LazyLoadingListAdapter(Context context, View header) {
        super(context, header);
    }

    @Override
    protected int getNullLayoutId() {
        return R.layout.navigation_list_null_item;
    }
}
