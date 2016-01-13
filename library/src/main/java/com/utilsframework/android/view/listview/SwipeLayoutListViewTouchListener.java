package com.utilsframework.android.view.listview;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by CM on 9/10/2015.
 */
public class SwipeLayoutListViewTouchListener implements ListView.OnTouchListener {
    private View swipeContainer;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        AbsListView listView = (AbsListView) v;
        int topRowVerticalPosition = listView.getChildCount() == 0 ? 0 : listView.getChildAt(0).getTop();
        int firstVisibleItem = listView.getFirstVisiblePosition();
        swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition == 0);
        return false;
    }

    public SwipeLayoutListViewTouchListener(View swipeContainer) {
        this.swipeContainer = swipeContainer;
    }
}
