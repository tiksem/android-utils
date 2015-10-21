package com.utilsframework.android.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.utilsframework.android.R;
import com.utilsframework.android.adapters.ViewArrayAdapter;
import com.utilsframework.android.menu.SortListener;
import com.utilsframework.android.menu.SortMenuAction;

import java.util.List;

/**
 * Created by stykhonenko on 19.10.15.
 */
public abstract class ListViewFragment<T> extends Fragment implements SortListener {
    private AbsListView listView;
    private Parcelable listViewState;
    private ViewArrayAdapter<T, ?> adapter;
    private SortMenuAction sortAction;

    protected abstract ViewArrayAdapter<T, ?> createAdapter();
    protected abstract List<T> createList();
    protected abstract void onListItemClicked(T item, int position);

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
            listView.onRestoreInstanceState(listViewState);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClicked(adapter.getElement(position), position);
            }
        });
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

    public AbsListView getListView() {
        return listView;
    }

    @Override
    public void onSortOrderChanged(int newSortOrder) {
        updateList();
    }

    protected final int getSortOrder() {
        if (sortAction == null) {
            return 0;
        }

        return sortAction.getSortOrder();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int sortMenuId = getSortMenuId();
        if (sortMenuId != 0) {
            inflater.inflate(sortMenuId, menu);
            sortAction = new SortMenuAction(menu, getSortMenuGroupId());
            sortAction.setSortListener(this);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    protected int getSortMenuId() {
        return 0;
    }

    protected int getSortMenuGroupId() {
        return 0;
    }
}
