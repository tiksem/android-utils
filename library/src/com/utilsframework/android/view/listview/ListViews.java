package com.utilsframework.android.view.listview;

import android.view.View;
import android.widget.AbsListView;
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
}
