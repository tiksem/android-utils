package com.utilsframework.android.menu;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stykhonenko on 01.12.15.
 */
public class MenuUtils {
    public static List<MenuItem> getAllItemsRecursive(Menu menu, List<MenuItem> out) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            MenuItem item = menu.getItem(i);
            out.add(item);

            if(item.hasSubMenu()){
                getAllItemsRecursive(item.getSubMenu(), out);
            }
        }

        return out;
    }

    public static List<MenuItem> getAllItemsRecursive(Menu menu) {
        return getAllItemsRecursive(menu, new ArrayList<MenuItem>());
    }

    public static void setTintColor(MenuItem menuItem, int tintColor) {
        Drawable newIcon = menuItem.getIcon();
        if (newIcon != null) {
            newIcon.mutate().setColorFilter(tintColor, PorterDuff.Mode.SRC_IN);
            menuItem.setIcon(newIcon);
        }
    }

    public static void setTintColorForAllItems(Menu menu, int tintColor) {
        for (MenuItem menuItem : getAllItemsRecursive(menu)) {
            setTintColor(menuItem, tintColor);
        }
    }
}
