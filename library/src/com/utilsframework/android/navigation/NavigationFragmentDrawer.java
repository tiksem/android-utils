package com.utilsframework.android.navigation;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
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
        int viewId = view.getId();
        if(viewId == currentSelectedItem){
            return;
        }

        this.currentSelectedItem = viewId;
        Fragment fragment = fragmentFactory.createFragmentBySelectedItem(viewId);
        GuiUtilities.replaceFragment(activity, getContentId(), fragment);
        hide();
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

        Fragment fragment = fragmentFactory.createFragmentBySelectedItem(currentSelectedItem);
        GuiUtilities.addFragment(activity, getContentId(), fragment);

        initDrawableToggle();
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
