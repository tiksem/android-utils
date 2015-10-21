package com.utilsframework.android.menu;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by stykhonenko on 21.10.15.
 */
public class SortMenuAction {
    private SortListener sortListener;
    private int sortOrder;

    public SortMenuAction(Menu menu, int sortGroupId) {
        MenuManager menuManager = new MenuManager(menu);
        sortOrder = menuManager.getFirstCheckedItemOfGroup(sortGroupId).getItemId();

        menuManager.setOnClickListenersForAllItemsInGroup(sortGroupId, new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (sortListener != null) {
                    sortOrder = item.getItemId();
                    sortListener.onSortOrderChanged(sortOrder);
                }
                item.setChecked(true);

                return true;
            }
        });
    }

    public SortListener getSortListener() {
        return sortListener;
    }

    public void setSortListener(SortListener sortListener) {
        this.sortListener = sortListener;
    }

    public int getSortOrder() {
        return sortOrder;
    }
}
