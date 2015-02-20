package com.utilsframework.android.navigation;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import com.utils.framework.Predicate;
import com.utilsframework.android.R;
import com.utilsframework.android.fragments.FragmentReplacer;
import com.utilsframework.android.view.GuiUtilities;

import java.util.List;

/**
 * Created by CM on 12/26/2014.
 */
public abstract class NavigationDrawerActivity extends Activity implements FragmentFactory {
    private NavigationFragmentDrawer navigationDrawer;
    private View navigationView;
    private List<View> navigationViewChildren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawable_activity);

        ViewStub navigationStub = (ViewStub) findViewById(R.id.navigationStub);
        navigationStub.setLayoutResource(getNavigationLayoutId());
        navigationView = navigationStub.inflate();
        navigationViewChildren = GuiUtilities.getAllChildrenRecursive(navigationView,
                new Predicate<View>() {
                    @Override
                    public boolean check(View item) {
                        return item.getId() != View.NO_ID;
                    }
                });

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

            @Override
            protected void onNavigationItemClick(View selectedView) {
                NavigationDrawerActivity.this.onNavigationItemSelected(selectedView);
                for(View view : navigationViewChildren){
                    if(view != selectedView){
                        onNavigationItemDeselected(view);
                    }
                }
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

    protected void onNavigationItemDeselected(View view) {
        if (view.isSelected()) {
            view.setSelected(false);
            view.refreshDrawableState();
        }
    }

    protected void onNavigationItemSelected(View view) {
        if (!view.isSelected()) {
            view.setSelected(true);
            view.refreshDrawableState();
        }
    }

    public void replaceFragment(Fragment newFragment, int navigationLevel) {
        navigationDrawer.replaceFragment(newFragment, navigationLevel);
    }
}
