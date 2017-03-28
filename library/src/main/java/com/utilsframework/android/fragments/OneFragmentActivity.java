package com.utilsframework.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.utilsframework.android.R;
import com.utilsframework.android.navdrawer.ActionBarTitleProvider;
import com.utilsframework.android.navdrawer.FragmentsNavigationInterface;
import com.utilsframework.android.view.GuiUtilities;

/**
 * Created by CM on 7/20/2015.
 */
public class OneFragmentActivity extends AppCompatActivity implements FragmentsNavigationInterface {
    public static final String FRAGMENT = "fragment";
    public static final String ARGS = "args";
    public static final String TOOL_BAR_LAYOUT_ID = "toolbarLayoutId";

    private FragmentsStackManager fragmentsStackManager;

    public static void start(Context context, Class<? extends Fragment> fragmentClass, Bundle args,
                             int toolbarLayoutId) {
        Intent intent = getStartIntent(context, fragmentClass, args, toolbarLayoutId);
        context.startActivity(intent);
    }

    public static void start(Context context, Class<? extends Fragment> fragmentClass, Bundle args) {
        Intent intent = getStartIntent(context, fragmentClass, args, 0);
        context.startActivity(intent);
    }

    public static void start(Context context, Class<? extends Fragment> fragmentClass) {
        Intent intent = getStartIntent(context, fragmentClass, null, 0);
        context.startActivity(intent);
    }

    public static void startForResult(Activity activity, int requestCode,
                                      Class<? extends Fragment> fragmentClass) {
        Intent intent = getStartIntent(activity, fragmentClass, null, 0);
        activity.startActivityForResult(intent, requestCode);
    }

    public static Intent getStartIntent(Context context,
                                        Class<? extends Fragment> fragmentClass,
                                        Bundle args,
                                        int toolbarLayoutId) {
        Intent intent = new Intent(context, OneFragmentActivity.class);
        intent.putExtra(FRAGMENT, fragmentClass.getCanonicalName());
        if (args != null) {
            intent.putExtra(ARGS, args);
        }
        if (toolbarLayoutId != 0) {
            intent.putExtra(TOOL_BAR_LAYOUT_ID, toolbarLayoutId);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Fragment fragment = createInitialFragment(intent);
        setContentView(R.layout.one);

        fragmentsStackManager = new FragmentsStackManager(this, R.id.one);
        fragmentsStackManager.replaceCurrentFragmentWithoutAddingToBackStack(fragment);

        int toolBarLayoutId = intent.getIntExtra(TOOL_BAR_LAYOUT_ID, 0);

        if (toolBarLayoutId != 0) {
            Toolbar toolbar = (Toolbar) View.inflate(this, toolBarLayoutId, null);
            ((ViewGroup)findViewById(R.id.one).getParent()).addView(toolbar, 0);
            setSupportActionBar(toolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            updateActionBarTitle(fragment);
        }
    }

    protected Fragment createInitialFragment(Intent intent) {
        String fragmentClassName = intent.getStringExtra(FRAGMENT);
        return Fragment.instantiate(this, fragmentClassName, intent.getBundleExtra(ARGS));
    }

    private void updateActionBarTitle(Fragment fragment) {
        final ActionBar actionBar = getSupportActionBar();
        if (fragment instanceof ActionBarTitleProvider) {
            final ActionBarTitleProvider actionBarTitleProvider = (ActionBarTitleProvider) fragment;
            Fragments.executeWhenViewCreated(fragment, new GuiUtilities.OnViewCreated() {
                @Override
                public void onViewCreated(View view) {
                    String title = actionBarTitleProvider.getActionBarTitle();
                    if (title != null && actionBar != null) {
                        actionBar.setTitle(title);
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void replaceFragment(Fragment newFragment, int navigationLevel) {
        fragmentsStackManager.replaceFragment(newFragment, navigationLevel);
        updateActionBarTitle(newFragment);
    }

    @Override
    public void replaceCurrentFragmentWithoutAddingToBackStack(Fragment newFragment) {
        fragmentsStackManager.replaceCurrentFragmentWithoutAddingToBackStack(newFragment);
        updateActionBarTitle(newFragment);
    }

    @Override
    public Fragment getLatestBackStackFragment() {
        return fragmentsStackManager.getLatestBackStackFragment();
    }

    @Override
    public void replaceFragment(int navigationLevel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.one);
    }
}
