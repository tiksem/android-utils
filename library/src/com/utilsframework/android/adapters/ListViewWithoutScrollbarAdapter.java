package com.utilsframework.android.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import com.utilsframework.android.view.ListViews;

/**
 * Created by CM on 7/7/2015.
 */
public class ListViewWithoutScrollbarAdapter extends BaseAdapter {
    private BaseAdapter adapter;
    private int count = -1;

    public ListViewWithoutScrollbarAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getCount() {
        if (count < 0) {
            return adapter.getCount();
        }

        return count;
    }

    @Override
    public Object getItem(int position) {
        return adapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return adapter.getItemId(position);
    }

    private void updateCount(AbsListView absListView, int height) {
        ViewGroup.LayoutParams layoutParams = absListView.getLayoutParams();
        layoutParams.height = height;
        absListView.setLayoutParams(layoutParams);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AbsListView listView = (AbsListView) parent;
        int listViewHeight = listView.getMeasuredHeight();
        View view = adapter.getView(position, convertView, parent);
        view.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        View lastVisibleItem = listView.getChildAt(listView.getChildCount() - 1);

        int bottom = lastVisibleItem.getBottom();
        listViewHeight += view.getMeasuredHeight();
        if (bottom > listViewHeight) {
            count = position;
            updateCount(listView, lastVisibleItem.getTop());
        } else if(bottom == listViewHeight) {
            count = position + 1;
            updateCount(listView, lastVisibleItem.getTop());
        }

        return view;
    }
}
