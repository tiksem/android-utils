package com.utilsframework.android.navdrawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import com.utils.framework.collections.*;
import com.utils.framework.collections.Stack;
import com.utilsframework.android.R;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.view.GuiUtilities;

import java.util.*;

/**
 * Created by CM on 12/26/2014.
 */
public abstract class NavigationHandler {
    private final AppCompatActivity activity;
    private FragmentFactory fragmentFactory;
    private int currentSelectedItem;
    private int navigationLevel = 0;
    private int currentSelectedTabIndex;
    private NavigationMode navigationMode = NavigationMode.SHOW_BACK_FOR_NESTED_LEVELS;
    private Set<FragmentManager.OnBackStackChangedListener> backStackChangedListeners =
            Collections.newSetFromMap(new WeakHashMap<FragmentManager.OnBackStackChangedListener, Boolean>());
    private Stack<Fragment> backStack = new LinkedStack<>();
    private NavigationDrawerMenuAdapter navigationDrawerMenuAdapter;
    private View navigationView;
    private TabsAdapter tabsAdapter;
    private DrawerLayoutAdapter drawerLayoutAdapter;

    private void clearBackStack() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        for(FragmentManager.OnBackStackChangedListener onBackStackChangedListener : backStackChangedListeners) {
            fragmentManager.removeOnBackStackChangedListener(onBackStackChangedListener);
        }
        backStackChangedListeners.clear();

        Fragments.clearBackStack(activity);
        backStack.clear();
    }

    private void selectFragment(int menuItemId, int navigationLevel, int tabIndex, boolean createFragment) {
        hide();

        if(menuItemId == currentSelectedItem && navigationLevel == this.navigationLevel){
            return;
        }

        tabsAdapter.removeAllTabs();
        int tabsCount = fragmentFactory.getTabsCount(menuItemId, navigationLevel);
        onTabsInit(tabsCount, navigationLevel);
        View tabsAdapterView = tabsAdapter.getView();
        if (tabsCount <= 1) {
            if (createFragment) {
                clearBackStack();
                Fragments.removeFragmentWithId(activity.getSupportFragmentManager(), getContentId());
                Fragment fragment = fragmentFactory.createFragmentBySelectedItem(menuItemId, 0, navigationLevel);
                Fragments.replaceFragment(activity, getContentId(), fragment);
            }
            if (tabsAdapterView != null) {
                tabsAdapterView.setVisibility(View.GONE);
            }
        } else {
            if (tabsAdapterView != null) {
                tabsAdapterView.setVisibility(View.VISIBLE);
            }
            initTabs(tabsCount, menuItemId, navigationLevel, createFragment, tabIndex);
        }

        this.currentSelectedItem = menuItemId;
        this.navigationLevel = navigationLevel;
        updateActionBarTitle();

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(navigationLevel != 0 ||
                    navigationMode != NavigationMode.NEVER_SHOW_NAVIGATION_TOGGLE);

            if (navigationMode == NavigationMode.SHOW_BACK_FOR_NESTED_LEVELS) {
                if (navigationLevel == 0) {
                    actionBar.setHomeAsUpIndicator(getToggleIconResourceId());
                } else {
                    actionBar.setHomeAsUpIndicator(null);
                }
            }
        }
    }

    private void initTabs(int tabsCount,
                          final int menuItemId,
                          final int navigationLevel,
                          final boolean createFragment,
                          final int selectedTabIndex) {
        tabsAdapter.removeAllTabs();
        tabsAdapter.setOnTabSelected(new TabsAdapter.OnTabSelected() {
            boolean shouldCreateFragment = createFragment;

            @Override
            public void onTabSelected(TabsAdapter.Tab tab) {
                if (menuItemId != currentSelectedItem) {
                    clearBackStack();
                    Fragments.removeFragmentWithId(activity.getSupportFragmentManager(), getContentId());
                }

                int tabIndex = tab.getIndex();
                if (shouldCreateFragment || selectedTabIndex != tabIndex) {
                    Fragment fragment =
                            fragmentFactory.createFragmentBySelectedItem(menuItemId, tabIndex,
                                    navigationLevel);
                    Fragments.replaceFragment(activity, getContentId(), fragment);
                }

                shouldCreateFragment = true;
                currentSelectedTabIndex = tabIndex;
            }
        });

        for (int i = 0; i < tabsCount; i++) {
            TabsAdapter.Tab tab = tabsAdapter.newTab(i == selectedTabIndex);
            fragmentFactory.initTab(menuItemId, i, navigationLevel, tab);
        }

        currentSelectedTabIndex = selectedTabIndex;
    }

    protected abstract void onTabsInit(int tabsCount, int navigationLevel);

    public void init(NavigationMode navigationMode) {
        this.navigationMode = navigationMode;

        int tabsCount = fragmentFactory.getTabsCount(currentSelectedItem, navigationLevel);
        onTabsInit(tabsCount, navigationLevel);
        final View tabsAdapterView = tabsAdapter.getView();
        if (tabsCount > 1) {
            if (tabsAdapterView != null) {
                tabsAdapterView.setVisibility(View.VISIBLE);
            }
            initTabs(tabsCount, currentSelectedItem, navigationLevel, true, 0);
        } else {
            if (tabsAdapterView != null) {
                tabsAdapterView.setVisibility(View.GONE);
            }
            Fragments.replaceFragment(activity, getContentId(),
                    fragmentFactory.createFragmentBySelectedItem(currentSelectedItem, 0, navigationLevel));
        }

        navigationDrawerMenuAdapter = createNavigationDrawerMenuAdapter(getNavigationViewId());
        navigationDrawerMenuAdapter.setOnItemSelectedListener(
                new NavigationDrawerMenuAdapter.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                performMenuItemSelection(id);
            }
        });

        navigationView = navigationDrawerMenuAdapter.getNavigationMenuView();
        navigationDrawerMenuAdapter.applySelectItemVisualStyle(currentSelectedItem);

        updateActionBarTitle();
        initDrawableToggle();
    }

    public void performMenuItemSelection(int id) {
        navigationDrawerMenuAdapter.applySelectItemVisualStyle(currentSelectedItem);
        selectFragment(id, 0, 0, true);
    }

    public void selectTab(int index) {
        tabsAdapter.selectTab(index);
    }

    public NavigationHandler(final AppCompatActivity activity,
                             FragmentFactory fragmentFactory,
                             int currentSelectedItem) {
        this.fragmentFactory = fragmentFactory;
        this.currentSelectedItem = currentSelectedItem;
        this.activity = activity;

        drawerLayoutAdapter = createDrawerLayoutAdapter();
        tabsAdapter = createTabsAdapter();

        ViewStub toolbarStub = (ViewStub) activity.findViewById(getToolBarStubId());
        toolbarStub.setLayoutResource(getToolbarLayoutId());
        Toolbar toolbar = (Toolbar) toolbarStub.inflate();
        activity.setSupportActionBar(toolbar);
    }

    public void updateActionBarTitle() {
        activity.getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                ActionBar actionBar = activity.getSupportActionBar();
                if (actionBar == null) {
                    return;
                }

                String title = null;
                Fragment fragment = activity.getSupportFragmentManager().findFragmentById(getContentId());
                if (fragment instanceof ActionBarTitleProvider) {
                    title = ((ActionBarTitleProvider) fragment).getActionBarTitle();
                }
                if (title == null) {
                    title = getActionBarTitle(currentSelectedItem, currentSelectedTabIndex, navigationLevel);
                }

                actionBar.setTitle(title);
            }
        });
    }

    private void initDrawableToggle() {
        drawerLayoutAdapter.setListener(new DrawerLayoutAdapter.Listener() {
            @Override
            public void onDrawerClosed(View view) {
                updateActionBarTitle();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                ActionBar actionBar = activity.getSupportActionBar();
                if (actionBar != null) {
                    String title = getActionBarTitleWhenNavigationIsShown(currentSelectedItem);
                    actionBar.setTitle(title);
                }
            }
        });

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null && navigationMode != NavigationMode.NEVER_SHOW_NAVIGATION_TOGGLE) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(getToggleIconResourceId());
        }
    }

    public void show() {
        drawerLayoutAdapter.openDrawer(navigationView);
    }

    public void hide() {
        drawerLayoutAdapter.closeDrawer(navigationView);
    }

    protected abstract int getContentId();
    protected abstract int getNavigationViewId();
    protected abstract int getToolBarStubId();
    protected abstract int getToolbarLayoutId();
    protected abstract NavigationDrawerMenuAdapter createNavigationDrawerMenuAdapter(int navigationViewId);
    protected abstract TabsAdapter createTabsAdapter();
    protected abstract DrawerLayoutAdapter createDrawerLayoutAdapter();

    protected String getActionBarTitle(int currentSelectedItem, int tabIndex, int navigationLevel) {
        return GuiUtilities.getApplicationName(activity);
    }

    protected String getActionBarTitleWhenNavigationIsShown(int currentSelectedItem) {
        return GuiUtilities.getApplicationName(activity);
    }

    // Override this method only for API 21+
    protected int getToggleIconResourceId() {
        return R.drawable.ic_menu_white;
    }

    public void onBackPressed() {
        Fragments.removeFragmentWithId(activity.getSupportFragmentManager(), getContentId());
    }

    public Fragment getCurrentFragment() {
        return activity.getSupportFragmentManager().findFragmentById(getContentId());
    }

    public void replaceFragment(Fragment newFragment, final int navigationLevel) {
        final int lastNavigationLevel = this.navigationLevel;
        final int lastTabIndex = currentSelectedTabIndex;

        if (newFragment == null) {
            newFragment = fragmentFactory.createFragmentBySelectedItem(currentSelectedItem, 0, navigationLevel);
        }

        final int backStackEntryCount = activity.getSupportFragmentManager().getBackStackEntryCount();
        Fragments.replaceOrAddFragmentAndAddToBackStack(activity, getContentId(), newFragment);
        selectFragment(currentSelectedItem, navigationLevel, 0, false);

        FragmentManager.OnBackStackChangedListener onBackStackChangedListener =
                new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (NavigationHandler.this.navigationLevel == navigationLevel) {
                    if (backStackEntryCount != activity.getSupportFragmentManager().getBackStackEntryCount()) {
                        return;
                    }

                    backStack.pop();
                    selectFragment(currentSelectedItem, lastNavigationLevel, lastTabIndex, false);
                    activity.getSupportFragmentManager().removeOnBackStackChangedListener(this);
                    backStackChangedListeners.remove(this);
                }
            }
        };
        activity.getSupportFragmentManager().addOnBackStackChangedListener(
                onBackStackChangedListener);
        backStackChangedListeners.add(onBackStackChangedListener);
        backStack.push(getCurrentFragment());
    }

    public void replaceCurrentFragmentWithoutAddingToBackStack(Fragment newFragment) {
        backStack.replaceTop(newFragment);
        Fragments.replaceFragment(activity, getContentId(), newFragment);
    }

    public void replaceFragment(final int navigationLevel) {
        replaceFragment(null, navigationLevel);
    }

    public int getCurrentSelectedItem() {
        return currentSelectedItem;
    }

    public void handleHomeButtonClick() {
        if (navigationMode == NavigationMode.ALWAYS_SHOW_NAVIGATION_TOGGLE || navigationLevel == 0) {
            toggleDrawer();
        } else {
            activity.onBackPressed();
        }
    }

    public void toggleDrawer() {
        if (drawerLayoutAdapter.isDrawerOpened(navigationView)) {
            drawerLayoutAdapter.closeDrawer(navigationView);
        } else {
            drawerLayoutAdapter.openDrawer(navigationView);
        }
    }

    public Fragment getLatestBackStackFragment() {
        return backStack.top();
    }

    public NavigationMode getNavigationMode() {
        return navigationMode;
    }

    public void setNavigationMode(NavigationMode navigationMode) {
        if (navigationMode == null) {
            throw new NullPointerException();
        }

        this.navigationMode = navigationMode;
    }
}
