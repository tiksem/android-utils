package com.utilsframework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.utils.framework.algorithms.Search;
import com.utils.framework.suggestions.SuggestionsProvider;
import com.utilsframework.android.R;
import com.utilsframework.android.threading.AsyncOperationCallback;

/**
 * Created by CM on 10/13/2014.
 */
public class SearchView extends FrameLayout {
    public interface Searcher<T> {
        T search(String query);
        void onSearchFinished(T result);
    }

    private AutoCompleteTextView searchField;
    private Searcher searcher;
    private String searchProgressDialogText = "Please wait...";

    private void init() {
        inflate(getContext(), getRootLayoutId(), this);
        searchField = (AutoCompleteTextView) findViewById(getSearchFieldViewId());
    }

    private void onSearchRequested() {
        if(searcher == null){
            return;
        }

        final String query = searchField.getText().toString();
        Alerts.runAsyncOperationWithCircleLoading(getContext(), searchProgressDialogText,
                new AsyncOperationCallback<Object>() {
                    @Override
                    public Object runOnBackground() {
                        return searcher.search(query);
                    }

                    @Override
                    public void onFinish(Object result) {
                        searcher.onSearchFinished(result);
                    }
                });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER){
            onSearchRequested();
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    public SearchView(Context context) {
        super(context);
        init();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected int getRootLayoutId() {
        return R.layout.search_view;
    }

    protected int getSearchFieldViewId() {
        return R.id.search_field;
    }

    public <T extends Filterable & ListAdapter> void setSuggestionsAdapter(T adapter) {
        searchField.setAdapter(adapter);
    }

    public Searcher getSearcher() {
        return searcher;
    }

    public <T> void setSearcher(Searcher<T> searcher) {
        this.searcher = searcher;
    }

    public String getSearchProgressDialogText() {
        return searchProgressDialogText;
    }

    public void setSearchProgressDialogText(String searchProgressDialogText) {
        this.searchProgressDialogText = searchProgressDialogText;
    }
}
