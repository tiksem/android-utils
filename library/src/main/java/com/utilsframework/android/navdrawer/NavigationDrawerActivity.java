package com.utilsframework.android.navdrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import com.utilsframework.android.R;

/**
 * Created by CM on 12/26/2014.
 */
public abstract class NavigationDrawerActivity extends AppCompatActivity implements NavigationActivityInterface {
    private NavigationFragmentDrawer navigationDrawer;
    private boolean preventAutomaticInit = false;

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
        FragmentFactory fragmentFactory = createFragmentFactory();

        navigationDrawer = new NavigationFragmentDrawer(this, fragmentFactory,
                getCurrentSelectedNavigationItemId()) {
            @Override
            protected DrawerLayoutAdapter createDrawerLayoutAdapter() {
                return NavigationDrawerActivity.this.createDrawerLayoutAdapter();
            }

            @Override
            protected int getContentId() {
                return NavigationDrawerActivity.this.getContentId();
            }

            @Override
            protected int getNavigationViewId() {
                return R.id.navigation;
            }

            @Override
            protected String getActionBarTitle(int currentSelectedItem, int tabIndex, int navigationLevel) {
                String title = NavigationDrawerActivity.this.getActionBarTitle(currentSelectedItem,
                        tabIndex, navigationLevel);
                if(title == null){
                    title = super.getActionBarTitle(currentSelectedItem, tabIndex, navigationLevel);
                }

                return title;
            }

            @Override
            protected String getActionBarTitleWhenNavigationIsShown(int currentSelectedItem) {
                String title = NavigationDrawerActivity.this.
                        getActionBarTitleWhenNavigationIsShown(currentSelectedItem);
                if(title == null){
                    title = super.getActionBarTitleWhenNavigationIsShown(currentSelectedItem);
                }

                return title;
            }

            @Override
            protected int getToolBarStubId() {
                return NavigationDrawerActivity.this.getToolBarStubId();
            }

            @Override
            protected int getToolbarLayoutId() {
                return NavigationDrawerActivity.this.getToolbarLayoutId();
            }

            @Override
            protected NavigationDrawerMenuAdapter createNavigationDrawerMenuAdapter(int navigationViewId) {
                return NavigationDrawerActivity.this.createNavigationDrawerMenuAdapter(navigationViewId);
            }

            @Override
            protected TabsAdapter createTabsAdapter() {
                return NavigationDrawerActivity.this.createTabsAdapter();
            }

            @Override
            protected void onTabsInit(int tabsCount, int navigationLevel) {
                NavigationDrawerActivity.this.onTabsInit(tabsCount, navigationLevel);
            }
        };
        navigationDrawer.init(getNavigationMode());
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
        navigationDrawer.replaceFragment(newFragment, navigationLevel);
    }

    @Override
    public void replaceFragment(int navigationLevel) {
        navigationDrawer.replaceFragment(navigationLevel);
    }

    @Override
    public Fragment getCurrentFragment() {
        return navigationDrawer.getCurrentFragment();
    }

    public Fragment getLatestBackStackFragment() {
        return navigationDrawer.getLatestBackStackFragment();
    }

    @Override
    public void onBackPressed() {
        navigationDrawer.onBackPressed();
        super.onBackPressed();
    }

    public void updateActionBarTitle() {
        navigationDrawer.updateActionBarTitle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigationDrawer.handleHomeButtonClick();
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
        navigationDrawer.performMenuItemSelection(id);
    }

    public void selectTab(int index) {
        navigationDrawer.selectTab(index);
    }

    protected abstract NavigationDrawerMenuAdapter createNavigationDrawerMenuAdapter(int navigationViewId);

    protected TabsAdapter createTabsAdapter() {
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
