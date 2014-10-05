package com.utilsframework.android.view;

import android.content.Context;
import android.graphics.Rect;
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
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            performFiltering(getText(), 0);
        }
    }
}
