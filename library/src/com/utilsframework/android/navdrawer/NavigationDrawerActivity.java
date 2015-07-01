package com.utilsframework.android.navdrawer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import com.utilsframework.android.R;

import java.util.List;

/**
 * Created by CM on 12/26/2014.
 */
public abstract class NavigationDrawerActivity extends AppCompatActivity implements FragmentFactory {
    private NavigationFragmentDrawer navigationDrawer;
    private View navigationView;
    private List<View> navigationViewChildren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawable_activity);

        navigationDrawer = new NavigationFragmentDrawer(this, this,
                getCurrentSelectedNavigationItemId()) {
            @Override
            protected int getDrawerLayoutId() {
                return R.id.drawer_layout;
            }

            @Override
            protected int getContentId() {
                return R.id.content;
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
            protected int getTabLayoutId() {
                return R.id.tabs;
            }

            @Override
            protected int getToolBarLayoutId() {
                return R.id.toolbar;
            }

            @Override
            protected int getMenuId() {
                int navigationMenuId = getNavigationMenuId();
                if (navigationMenuId == 0) {
                    throw new IllegalStateException("getNavigationMenuId returns 0");
                }

                return navigationMenuId;
            }
        };
        navigationDrawer.init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected abstract int getCurrentSelectedNavigationItemId();
    protected abstract int getNavigationMenuId();
    protected String getActionBarTitle(int selectedItemId, int tabIndex, int navigationLevel) {
        return null;
    }

    protected String getActionBarTitleWhenNavigationIsShown(int currentSelectedItem) {
        return null;
    }

    public void replaceFragment(Fragment newFragment, int navigationLevel) {
        navigationDrawer.replaceFragment(newFragment, navigationLevel);
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content);
    }

    @Override
    public void onBackPressed() {
        navigationDrawer.onBackPressed();
        super.onBackPressed();
    }

    public View getSelectedView() {
        int currentSelectedItem = navigationDrawer.getCurrentSelectedItem();
        return navigationView.findViewById(currentSelectedItem);
    }

    public void updateActionBarTitle() {
        navigationDrawer.updateActionBarTitle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigationDrawer.toggleDrawer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
