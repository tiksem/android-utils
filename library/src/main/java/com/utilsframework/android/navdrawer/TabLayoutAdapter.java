package com.utilsframework.android.navdrawer;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewStub;

/**
 * Created by stykhonenko on 19.10.15.
 */
public class TabLayoutAdapter implements TabsAdapter {
    private TabLayout tabLayout;

    private static class TabHolder implements Tab {
        private TabLayout.Tab tab;

        public TabHolder(TabLayout.Tab tab) {
            this.tab = tab;
        }

        @Override
        public void setText(CharSequence text) {
            tab.setText(text);
        }

        @Override
        public void setText(int id) {
            tab.setText(id);
        }

        @Override
        public int getIndex() {
            return tab.getPosition();
        }

        @Override
        public Object getTabHandler() {
            return tab;
        }

        @Override
        public void setIcon(int resourceId) {
            tab.setIcon(resourceId);
        }
    }

    public TabLayoutAdapter(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    @Override
    public void setOnTabSelected(final OnTabSelected listener) {
        if (listener != null) {
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    listener.onTabSelected(new TabHolder(tab));
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } else {
            tabLayout.setOnTabSelectedListener(null);
        }
    }

    @Override
    public Tab getTab(int index) {
        return new TabHolder(tabLayout.getTabAt(index));
    }

    @Override
    public void selectTab(int index) {
        tabLayout.getTabAt(index).select();
    }

    @Override
    public void setupViewPager(ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);
    }
}
