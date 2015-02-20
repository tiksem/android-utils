package com.utilsframework.android.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import com.utils.framework.strings.Strings;

/**
 * Created by CM on 10/5/2014.
 */
public class EditTextWithSuggestions extends AutoCompleteTextView {
    private boolean displayDefaultValueOnNoItemSelected = false;
    private String defaultValue = "";
    private AdapterView.OnItemClickListener onItemClickListener;
    private String lastSelectedItem;

    private void init() {
        super.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastSelectedItem = getText().toString();

                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }

    public EditTextWithSuggestions(Context context) {
        super(context);
        init();
    }

    public EditTextWithSuggestions(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextWithSuggestions(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        onItemClickListener = l;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            if(displayDefaultValueOnNoItemSelected){
                if(Strings.charSequenceEquals(defaultValue, getText())){
                    setText("");
                }
            }

            performFiltering(getText(), 0);
        } else if(displayDefaultValueOnNoItemSelected && !getText().toString().equals(lastSelectedItem)) {
            setText(defaultValue);
        }
    }

    public boolean isDisplayDefaultValueOnNoItemSelected() {
        return displayDefaultValueOnNoItemSelected;
    }

    public void setDisplayDefaultValueOnNoItemSelected(boolean displayDefaultValueOnNoItemSelected) {
        this.displayDefaultValueOnNoItemSelected = displayDefaultValueOnNoItemSelected;
    }
}
