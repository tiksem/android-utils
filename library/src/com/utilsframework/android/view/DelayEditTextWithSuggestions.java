package com.utilsframework.android.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

public class DelayEditTextWithSuggestions extends EditTextWithSuggestions {
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 750;
 
    private int autoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;
    private ProgressBar mLoadingIndicator;

    private Runnable performFiltering;
    private Handler handler = new Handler();

    public DelayEditTextWithSuggestions(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setLoadingIndicator(ProgressBar progressBar) {
        mLoadingIndicator = progressBar;
    }
 
    public void setAutoCompleteDelay(int autoCompleteDelay) {
        this.autoCompleteDelay = autoCompleteDelay;
    }

    public int getAutoCompleteDelay() {
        return autoCompleteDelay;
    }

    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        if (performFiltering != null) {
            handler.removeCallbacks(performFiltering);
        }

        performFiltering = new Runnable() {
            @Override
            public void run() {
                DelayEditTextWithSuggestions.super.performFiltering(text, keyCode);
            }
        };

        handler.postDelayed(performFiltering, autoCompleteDelay);
    }
 
    @Override
    public void onFilterComplete(int count) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.GONE);
        }
        super.onFilterComplete(count);
    }
}