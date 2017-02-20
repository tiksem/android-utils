package com.utilsframework.android.navdrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.utilsframework.android.R;

/**
 * Created by CM on 12/26/2014.
 */
public abstract class NavigationActivity extends AppCompatActivity implements NavigationActivityInterface {
    private NavigationHandler navigationHandler;
    private boolean preventAutomaticInit = false;
    private FragmentFactory fragmentFactory;

    public boolean preventAutomaticInit() {
        return preventAutomaticInit;
    }

    public void setPreventAutomaticInit(boolean preventAutomaticInit) {
        this.preventAutomaticInit = preventAutomaticInit;
    }

    protected void onBeforeNavigationDrawerInit() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeNavigationDrawerInit();
        if (!preventAutomaticInit) {
            init();
        }
    }

    protected void init() {
        setContentView(getRootLayoutId());
        fragmentFactory = createFragmentFactory();
        if (fragmentFactory == null) {
            throw new NullPointerException("createFragmentFactory returns null");
        }

        navigationHandler = new NavigationHandler(this, fragmentFactory,
                getCurrentSelectedNavigationItemId()) {
            @Override
            protected DrawerLayoutAdapter createDrawerLayoutAdapter() {
                return NavigationActivity.this.createDrawerLayoutAdapter();
            }

            @Override
            protected int getContentId() {
                return NavigationActivity.this.getContentId();
            }

            @Override
            protected int getNavigationViewId() {
                return R.id.navigation;
            }

            @Override
            protected String getActionBarTitle(int currentSelectedItem, int tabIndex, int navigationLevel) {
                String title = NavigationActivity.this.getActionBarTitle(currentSelectedItem,
                        tabIndex, navigationLevel);
                if(title == null){
                    title = super.getActionBarTitle(currentSelectedItem, tabIndex, navigationLevel);
                }

                return title;
            }

            @Override
            protected String getActionBarTitleWhenNavigationIsShown(int currentSelectedItem) {
                String title = NavigationActivity.this.
                        getActionBarTitleWhenNavigationIsShown(currentSelectedItem);
                if(title == null){
                    title = super.getActionBarTitleWhenNavigationIsShown(currentSelectedItem);
                }

                return title;
            }

            @Override
            protected int getToolBarStubId() {
                return NavigationActivity.this.getToolBarStubId();
            }

            @Override
            protected int getToolbarLayoutId() {
                return NavigationActivity.this.getToolbarLayoutId();
            }

            @Override
            protected NavigationDrawerMenuAdapter createNavigationDrawerMenuAdapter(int navigationViewId) {
                return NavigationActivity.this.createNavigationDrawerMenuAdapter(navigationViewId);
            }

            @Override
            protected TabsAdapter createTabsAdapter() {
                return NavigationActivity.this.createTabsAdapter();
            }

            @Override
            protected void onTabsInit(int tabsCount, int navigationLevel) {
                NavigationActivity.this.onTabsInit(tabsCount, navigationLevel);
            }
        };
        navigationHandler.init(getNavigationMode());
    }

    protected int getContentId() {
        return R.id.content;
    }

    protected int getRootLayoutId() {
        return R.layout.navigation_drawable_activity;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected abstract int getCurrentSelectedNavigationItemId();
    protected abstract FragmentFactory createFragmentFactory();

    protected String getActionBarTitle(int selectedItemId, int tabIndex, int navigationLevel) {
        return null;
    }

    protected String getActionBarTitleWhenNavigationIsShown(int currentSelectedItem) {
        return null;
    }

    @Override
    public void replaceFragment(Fragment newFragment, int navigationLevel) {
        navigationHandler.replaceFragment(newFragment, navigationLevel);
    }

    @Override
    public void replaceFragment(int navigationLevel) {
        navigationHandler.replaceFragment(navigationLevel);
    }

    @Override
    public void replaceCurrentFragmentWithoutAddingToBackStack(Fragment newFragment) {
        navigationHandler.replaceCurrentFragmentWithoutAddingToBackStack(newFragment);
    }

    @Override
    public Fragment getCurrentFragment() {
        return navigationHandler.getCurrentFragment();
    }

    public Fragment getLatestBackStackFragment() {
        return navigationHandler.getLatestBackStackFragment();
    }

    @Override
    public void onBackPressed() {
        navigationHandler.onBackPressed();
        super.onBackPressed();
    }

    public void updateActionBarTitle() {
        navigationHandler.updateActionBarTitle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigationHandler.handleHomeButtonClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected int getToolbarLayoutId() {
        return R.layout.toolbar;
    }

    protected int getTabLayoutId() {
        return R.layout.tabs;
    }

    private void performMenuItemSelection(int id) {
        navigationHandler.performMenuItemSelection(id);
    }

    public void selectTab(int index) {
        navigationHandler.selectTab(index);
    }

    protected abstract NavigationDrawerMenuAdapter createNavigationDrawerMenuAdapter(int navigationViewId);

    protected TabsAdapter createTabsAdapter() {
        if (fragmentFactory instanceof NoTabsFragmentFactory) {
            return new NoTabsAdapter();
        }

        return TabLayoutAdapter.fromViewStub(this, getTabsStub(), getTabLayoutId());
    }

    protected int getTabsStub() {
        return R.id.tabsStub;
    }

    protected DrawerLayoutAdapter createDrawerLayoutAdapter() {
        return new AndroidSupportDrawerLayoutAdapter(this, R.id.drawer_layout);
    }

    public NavigationMode getNavigationMode() {
        return NavigationMode.SHOW_BACK_FOR_NESTED_LEVELS;
    }

    protected void onTabsInit(int tabsCount, int navigationLevel) {

    }

    protected int getToolBarStubId() {
        return R.id.toolbarStub;
    }
}
