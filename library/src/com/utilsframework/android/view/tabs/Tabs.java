package com.utilsframework.android.view.tabs;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CM on 12/26/2014.
 */
public class Tabs {
    public static TabsDestroyer initFragmentTabs(Activity activity, final int fragmentContainerId,
                                                 List<FragmentTabInfo> tabsInfo) {
        final ActionBar actionBar = activity.getActionBar();
        if(actionBar == null){
            throw new NullPointerException("actionBar == null");
        }

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        final List<ActionBar.Tab> tabs = new ArrayList<ActionBar.Tab>();
        for(final FragmentTabInfo tabInfo : tabsInfo){
            ActionBar.Tab tab = actionBar.newTab();
            tab.setText(tabInfo.text);

            tab.setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

                }
            });

            actionBar.addTab(tab);

            if(tabs.isEmpty()){
                actionBar.selectTab(tab);
            }

            tab.setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                    Fragment fragment = tabInfo.fragmentFactory.createFragment();
                    ft.add(fragmentContainerId, fragment).commit();
                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

                }
            });

            tabs.add(tab);
        }

        return new TabsDestroyer() {
            @Override
            public void destroyTabs() {
                for(ActionBar.Tab tab : tabs){
                    actionBar.removeTab(tab);
                }
            }
        };
    }
}
