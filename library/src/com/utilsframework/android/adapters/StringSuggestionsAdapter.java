package com.utilsframework.android.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.utilsframework.android.R;

/**
 * Created by CM on 7/9/2015.
 */
public class StringSuggestionsAdapter extends SuggestionsAdapter<String, Void> {
    public StringSuggestionsAdapter(Context context) {
        setViewArrayAdapter(new SingleViewArrayAdapter<String>(context) {
            @Override
            protected int getRootLayoutId(int viewType) {
                return getSuggestionLayoutId();
            }

            @Override
            protected void reuseView(String suggestion, Void aVoid, int position, View view) {
                ((TextView) view).setText(suggestion);
            }
        });
    }

    protected int getSuggestionLayoutId() {
        return R.layout.suggestion;
    }
}
