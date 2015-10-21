package com.utilsframework.android.menu;

import android.view.Menu;
import android.view.MenuItem;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Predicate;
import com.utils.framework.collections.map.MultiMap;
import com.utils.framework.collections.map.SetValuesHashMultiMap;

import java.util.Collection;

/**
 * Created by CM on 2/25/2015.
 */
public class MenuManager {
    private Menu menu;
    private MultiMap<Integer, MenuItem> itemsByGroups = new SetValuesHashMultiMap<Integer, MenuItem>();

    private void initMenu(Menu menu) {
        this.menu = menu;

        int size = menu.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = menu.getItem(i);
            int groupId = item.getGroupId();
            if(groupId > 0){
                itemsByGroups.put(groupId, item);
            }

            if(item.hasSubMenu()){
                initMenu(item.getSubMenu());
            }
        }
    }

    public MenuManager(Menu menu) {
        this.menu = menu;
        initMenu(menu);
    }

    public MenuItem getFirstCheckedItemOfGroup(int groupId) {
        Collection<MenuItem> items = itemsByGroups.getValues(groupId);
        if (items != null) {
            return CollectionUtils.find(items, new Predicate<MenuItem>() {
                @Override
                public boolean check(MenuItem item) {
                    return item.isChecked();
                }
            });
        }

        return null;
    }

    public Collection<MenuItem> getItemsByGroup(int groupId) {
        return itemsByGroups.getValues(groupId);
    }

    public void setOnClickListenersForAllItemsInGroup(int groupId, MenuItem.OnMenuItemClickListener listener) {
        for (MenuItem menuItem : getItemsByGroup(groupId)) {
            menuItem.setOnMenuItemClickListener(listener);
        }
    }
}
