package com.utilsframework.android.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import com.utilsframework.android.R;
import com.utilsframework.android.adapters.ViewArrayAdapter;

import java.util.List;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class ListViewFragment<T> extends Fragment {
    private AbsListView listView;
    private Parcelable listViewState;
    private ViewArrayAdapter<T, ?> adapter;

    protected abstract ViewArrayAdapter<T, ?> createAdapter();
    protected abstract List<T> createList();

    protected void updateList() {
        adapter.setElements(createList());
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getRootLayoutId(), null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (AbsListView) view.findViewById(getListViewId());
        if (adapter == null) {
            adapter = createAdapter();
            updateList();
        }

        listView.setAdapter(adapter);

        if (listViewState != null) {
            listView.onRestoreInstanceState(savedInstanceState);
        }
    }

    protected int getListViewId() {
        return R.id.list;
    }

    protected int getRootLayoutId() {
        return R.layout.list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listViewState = listView.onSaveInstanceState();
    }
}
