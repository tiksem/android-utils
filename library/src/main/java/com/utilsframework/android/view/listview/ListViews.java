package com.utilsframework.android.view.listview;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.utilsframework.android.UiLoopEvent;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by CM on 2/23/2015.
 */
public class ListViews {
    public static boolean hasScrollbar(AbsListView listView) {
        View lastVisibleItem = listView.getChildAt(listView.getChildCount() - 1);
        return lastVisibleItem.getBottom() >= listView.getMeasuredHeight();
    }

    public static void scrollListViewToPosition(AbsListView listView, int position){
        if(position == 0){
            position = 1;
        } else if(position < 0) {
            return;
        }

        listView.setSelection(position - 1);
    }

    public static View addHeader(ListView listView, int headerLayoutId) {
        View header = View.inflate(listView.getContext(), headerLayoutId, null);
        listView.addHeaderView(header);
        return header;
    }
}
