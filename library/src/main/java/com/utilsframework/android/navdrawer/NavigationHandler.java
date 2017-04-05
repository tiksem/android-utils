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
import com.utilsframework.android.threading.Tasks;
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
    private NavigationMode navigationMode = NavigationMode.SHOW_BACK_FOR_NESTED_LEVELS;
    private Set<FragmentManager.OnBackStackChangedListener> backStackChangedListeners =
            Collections.newSetFromMap(new WeakHashMap<FragmentManager.OnBackStackChangedListener, Boolean>());
    private Stack<Fragment> backStack = new LinkedStack<>();
    private MenuLayoutAdapter menuLayoutAdapter;
    private Queue<Runnable> whenDrawableClosedQueue = new ArrayDeque<>();

    private void clearBackStack() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        for(FragmentManager.OnBackStackChangedListener onBackStackChangedListener : backStackChangedListeners) {
            fragmentManager.removeOnBackStackChangedListener(onBackStackChangedListener);
        }
        backStackChangedListeners.clear();

        Fragments.clearBackStack(activity);
        backStack.clear();
    }

    private void executeWhenDrawerClosed(Runnable runnable) {
        if (!menuLayoutAdapter.isVisible()) {
            runnable.run();
        } else {
            whenDrawableClosedQueue.add(runnable);
        }
    }

    private void selectFragment(final int menuItemId,
                                final int navigationLevel,
                                final int tabIndex,
                                final boolean createFragment) {
        hideMenuLayout();

        executeWhenDrawerClosed(new Runnable() {
            @Override
            public void run() {
                NavigationHandler.this.currentSelectedItem = menuItemId;
                NavigationHandler.this.navigationLevel = navigationLevel;

                int tabsCount = fragmentFactory.getTabsCount(menuItemId, navigationLevel);
                if (createFragment) {
                    clearBackStack();
                    Fragments.removeFragmentWithId(activity.getSupportFragmentManager(),
                            getContentId());
                    Fragment fragment;
                    if (tabsCount <= 1) {
                        fragment = fragmentFactory.createFragmentBySelectedItem(
                                menuItemId, 0, navigationLevel);
                    } else {
                        fragment = TabsHolderFragment.create(tabIndex);
                    }
                    Fragments.replaceFragment(activity, getContentId(), fragment);
                }

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
        });
    }

    public void init(NavigationMode navigationMode) {
        this.navigationMode = navigationMode;

        menuLayoutAdapter.setListener(new MenuLayoutAdapter.Listener() {
            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {
                updateActionBarTitle();
                Tasks.executeAndClearQueue(whenDrawableClosedQueue);
            }

            @Override
            public void onItemSelected(int id) {
                performMenuItemSelection(id);
            }
        });

        selectFragment(currentSelectedItem, navigationLevel, 0, true);

        menuLayoutAdapter.applySelectItemVisualStyle(currentSelectedItem);

        updateActionBarTitle();

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null && navigationMode != NavigationMode.NEVER_SHOW_NAVIGATION_TOGGLE) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(getToggleIconResourceId());
        }
    }

    public void performMenuItemSelection(int id) {
        selectFragment(id, 0, 0, true);
    }

    public void selectTab(int index) {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof TabsHolderFragment) {
            TabsHolderFragment tabsHolderFragment = (TabsHolderFragment) currentFragment;
            tabsHolderFragment.selectTab(index);
        } else if (index != 0) {
            throw new IllegalStateException("No such tab");
        }
    }

    public NavigationHandler(final AppCompatActivity activity,
                             FragmentFactory fragmentFactory,
                             int currentSelectedItem) {
        this.fragmentFactory = fragmentFactory;
        this.currentSelectedItem = currentSelectedItem;
        this.activity = activity;

        menuLayoutAdapter = createDrawerLayoutAdapter();

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
                    title = getActionBarTitle(currentSelectedItem, getCurrentSelectedTabIndex(),
                            navigationLevel);
                }

                actionBar.setTitle(title);
            }
        });
    }

    public void showMenuLayout() {
        menuLayoutAdapter.open();
    }

    public void hideMenuLayout() {
        menuLayoutAdapter.close();
    }

    protected abstract int getContentId();
    protected abstract int getToolBarStubId();
    protected abstract int getToolbarLayoutId();
    protected abstract TabsAdapter createTabsAdapter(View tabsView);
    protected abstract MenuLayoutAdapter createDrawerLayoutAdapter();

    protected int getTabsLayout() {
        return R.layout.tabs;
    }

    protected String getActionBarTitle(int currentSelectedItem, int tabIndex, int navigationLevel) {
        return GuiUtilities.getApplicationName(activity);
    }

    // Override this method only for API 21+
    protected int getToggleIconResourceId() {
        return R.drawable.ic_menu_white;
    }

    public void onBackPressed(Runnable superBackPressed) {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof BackPressedListener) {
            BackPressedListener listener = (BackPressedListener) currentFragment;
            if (!listener.shouldOverrideDefaultBackPressedBehavior()) {
                callDefaultBackPressed(superBackPressed);
            }
        } else {
            callDefaultBackPressed(superBackPressed);
        }
    }

    private void callDefaultBackPressed(Runnable superBackPressed) {
        Fragments.removeFragmentWithId(activity.getSupportFragmentManager(),
                getContentId());
        superBackPressed.run();
    }

    public Fragment getCurrentFragment() {
        return activity.getSupportFragmentManager().findFragmentById(getContentId());
    }

    public void replaceFragment(Fragment newFragment, final int navigationLevel) {
        final int lastNavigationLevel = this.navigationLevel;
        final int lastTabIndex = getCurrentSelectedTabIndex();

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
        if (!backStack.isEmpty()) {
            backStack.replaceTop(newFragment);
        }
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
            toggleMenu();
        } else {
            activity.onBackPressed();
        }
    }

    public void toggleMenu() {
        if (menuLayoutAdapter.isOpen()) {
            menuLayoutAdapter.close();
        } else {
            menuLayoutAdapter.open();
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

    public FragmentFactory getFragmentFactory() {
        return fragmentFactory;
    }

    public int getNavigationLevel() {
        return navigationLevel;
    }

    public int getCurrentSelectedTabIndex() {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof TabsHolderFragment) {
            TabsHolderFragment tabsHolderFragment = (TabsHolderFragment) currentFragment;
            return tabsHolderFragment.getCurrentSelectedTabIndex();
        } else {
            return 0;
        }
    }
}
