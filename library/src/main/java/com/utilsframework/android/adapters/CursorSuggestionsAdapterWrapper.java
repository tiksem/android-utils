package com.utilsframework.android.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.ListAdapter;

/**
 * Created by CM on 10/15/2014.
 * Use this class when you want to use SearchView without CursorAdapter
 */
public class CursorSuggestionsAdapterWrapper extends CursorAdapter {
    private ListAdapter suggestionsAdapter;
    private Filterable filterable;

    public <T extends ListAdapter & Filterable>
    CursorSuggestionsAdapterWrapper(Context context, T adapter) {
        super(context, null, false);
        suggestionsAdapter = adapter;
        suggestionsAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }
        });
        filterable = adapter;
    }

    @Override
    public int getCount() {
        return suggestionsAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return suggestionsAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return suggestionsAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return suggestionsAdapter.getView(position, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        return filterable.getFilter();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        throw new RuntimeException("Should not be called");
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        throw new RuntimeException("Should not be called");
    }
}
