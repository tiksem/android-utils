package com.utilsframework.android.adapters;

import android.content.Context;
import android.view.View;

/**
 * Created by CM on 8/30/2014.
 */
public abstract class SingleViewArrayAdapter<Element> extends ViewArrayAdapter<Element, Void>{
    protected SingleViewArrayAdapter(Context context) {
        super(context);
    }

    @Override
    protected Void createViewHolder(View view) {
        return null;
    }
}
