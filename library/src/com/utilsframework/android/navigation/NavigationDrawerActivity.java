package com.utilsframework.android.navigation;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.View;
import android.view.ViewStub;
import com.utilsframework.android.R;

/**
 * Created by CM on 12/26/2014.
 */
public abstract class NavigationDrawerActivity extends Activity implements FragmentFactory{
    private NavigationFragmentDrawer navigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawable_activity);

        ViewStub navigationStub = (ViewStub) findViewById(R.id.navigationStub);
        navigationStub.setLayoutResource(getNavigationLayoutId());
        navigationStub.inflate();

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
            protected int getNavigationLayoutId() {
                return R.id.navigation;
            }

            @Override
            protected String getActionBarTitle(int currentSelectedItem) {
                String title = NavigationDrawerActivity.this.getActionBarTitle(currentSelectedItem);
                if(title == null){
                    title = super.getActionBarTitle(currentSelectedItem);
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
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected abstract int getNavigationLayoutId();
    protected abstract int getCurrentSelectedNavigationItemId();
    protected String getActionBarTitle(int selectedItemId) {
        return null;
    }

    protected String getActionBarTitleWhenNavigationIsShown(int currentSelectedItem) {
        return null;
    }
}
