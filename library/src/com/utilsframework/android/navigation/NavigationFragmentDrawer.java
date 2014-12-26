package com.utilsframework.android.navigation;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import com.utils.framework.Predicate;
import com.utilsframework.android.R;
import com.utilsframework.android.view.GuiUtilities;

import java.util.List;

/**
 * Created by CM on 12/26/2014.
 */
public abstract class NavigationFragmentDrawer {
    private final Activity activity;
    private DrawerLayout drawerLayout;
    private FragmentFactory fragmentFactory;
    private ViewGroup navigationLayout;
    private int currentSelectedItem;
    private ActionBarDrawerToggle drawerToggle;

    private void selectFragment(View view) {
        final int viewId = view.getId();
        if(viewId == currentSelectedItem){
            return;
        }

        this.currentSelectedItem = viewId;

        int tabsCount = fragmentFactory.getTabsCount(currentSelectedItem);
        if (tabsCount <= 1) {
            Fragment fragment = fragmentFactory.createFragmentBySelectedItem(viewId, 0);
            GuiUtilities.replaceFragment(activity, getContentId(), fragment);
            activity.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        } else {
            initTabs();
        }

        hide();
    }

    private void initTabs() {
        ActionBar actionBar = activity.getActionBar();
        if(actionBar == null){
            throw new NullPointerException("actionBar == null");
        }

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        int tabsCount = fragmentFactory.getTabsCount(currentSelectedItem);
        for (int i = 0; i < tabsCount; i++) {
            ActionBar.Tab tab = actionBar.newTab();
            fragmentFactory.initTab(currentSelectedItem, i, tab);

            final int tabIndex = i;
            ActionBar.TabListener listener = new ActionBar.TabListener() {
                @Override
                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                    Fragment fragment =
                            fragmentFactory.createFragmentBySelectedItem(currentSelectedItem, tabIndex);
                    ft.replace(getContentId(), fragment);
                }

                @Override
                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

                }

                @Override
                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

                }
            };
            tab.setTabListener(listener);

            actionBar.addTab(tab);
            if (tabIndex == 0) {
                actionBar.selectTab(tab);
                Fragment fragment =
                        fragmentFactory.createFragmentBySelectedItem(currentSelectedItem, tabIndex);
                GuiUtilities.replaceFragment(activity, getContentId(), fragment);
            }
        }
    }

    public NavigationFragmentDrawer(final Activity activity,
                                    FragmentFactory fragmentFactory,
                                    int currentSelectedItem) {
        this.fragmentFactory = fragmentFactory;
        this.currentSelectedItem = currentSelectedItem;
        this.activity = activity;

        drawerLayout = (DrawerLayout) activity.findViewById(getDrawerLayoutId());
        navigationLayout = (ViewGroup) activity.findViewById(getNavigationLayoutId());

        List<View> navigationViews = getNavigationViews();

        for(View navigationView : navigationViews){
            navigationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectFragment(v);
                }
            });
        }

        if(navigationLayout.findViewById(currentSelectedItem) == null){
            throw new IllegalArgumentException("Could not find view with " +
                    currentSelectedItem + " id");
        }

        initDrawableToggle();
        int tabsCount = fragmentFactory.getTabsCount(currentSelectedItem);
        if (tabsCount > 1) {
            initTabs();
        } else {
            GuiUtilities.addFragment(activity, getContentId(),
                    fragmentFactory.createFragmentBySelectedItem(currentSelectedItem, 0));
            activity.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }
    }

    private void initDrawableToggle() {
        drawerToggle = new ActionBarDrawerToggle(
                activity,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                getToggleIconResourceId(),  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                ActionBar actionBar = activity.getActionBar();
                if (actionBar != null) {
                    String title = getActionBarTitle(currentSelectedItem);
                    actionBar.setTitle(title);
                }
            }

            public void onDrawerOpened(View drawerView) {
                ActionBar actionBar = activity.getActionBar();
                if (actionBar != null) {
                    String title = getActionBarTitleWhenNavigationIsShown(currentSelectedItem);
                    actionBar.setTitle(title);
                }
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    private List<View> getNavigationViews() {
        return GuiUtilities.getAllChildrenRecursive(navigationLayout,
                new Predicate<View>() {
                    @Override
                    public boolean check(View item) {
                        if (item.getId() == View.NO_ID) {
                            return false;
                        }

                        if (item instanceof ViewGroup) {
                            ViewGroup viewGroup = (ViewGroup) item;
                            return viewGroup.getChildCount() == 0;
                        }

                        return true;
                    }
                });
    }

    public void show() {
        drawerLayout.openDrawer(navigationLayout);
    }

    public void hide() {
        drawerLayout.closeDrawer(navigationLayout);
    }

    protected abstract int getDrawerLayoutId();
    protected abstract int getContentId();
    protected abstract int getNavigationLayoutId();

    protected String getActionBarTitle(int currentSelectedItem) {
        return GuiUtilities.getApplicationName(activity);
    }

    protected String getActionBarTitleWhenNavigationIsShown(int currentSelectedItem) {
        return GuiUtilities.getApplicationName(activity);
    }

    protected int getToggleIconResourceId() {
        return R.drawable.ic_drawer;
    }
}
