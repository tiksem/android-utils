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

    public SortMenuAction(Menu menu, int sortGroupId, int initialSortOrder) {
        MenuManager menuManager = new MenuManager(menu);
        if (initialSortOrder == 0) {
            sortOrder = menuManager.getFirstCheckedItemOfGroup(sortGroupId).getItemId();
        } else {
            sortOrder = initialSortOrder;
            menu.findItem(initialSortOrder).setChecked(true);
        }

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
