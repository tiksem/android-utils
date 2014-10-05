package com.utilsframework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

/**
 * Created by CM on 10/5/2014.
 */
public class EditTextWithSuggestions extends AutoCompleteTextView {
    public EditTextWithSuggestions(Context context) {
        super(context);
    }

    public EditTextWithSuggestions(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextWithSuggestions(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        ListAdapter adapter = getAdapter();
        if(adapter != null && adapter instanceof BaseAdapter){
            BaseAdapter baseAdapter = (BaseAdapter) adapter;
            baseAdapter.notifyDataSetChanged();
        }
    }
}
