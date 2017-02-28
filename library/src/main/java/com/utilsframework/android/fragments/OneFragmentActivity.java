package com.utilsframework.android.fragments;

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
import com.utilsframework.android.navdrawer.NavigationActivityInterface;
import com.utilsframework.android.view.GuiUtilities;

/**
 * Created by CM on 7/20/2015.
 */
public class OneFragmentActivity extends AppCompatActivity implements NavigationActivityInterface {
    public static final String FRAGMENT = "fragment";
    public static final String ARGS = "args";
    public static final String TOOL_BAR_LAYOUT_ID = "toolbarLayoutId";

    private Fragment latestBackStackFragment;

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
        Fragments.replaceFragment(this, R.id.one, fragment);

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
                    actionBar.setTitle(actionBarTitleProvider.getActionBarTitle());
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
        latestBackStackFragment = getSupportFragmentManager().findFragmentById(R.id.one);
        Fragments.replaceOrAddFragmentAndAddToBackStack(this, R.id.one, newFragment);
        updateActionBarTitle(newFragment);
    }

    @Override
    public void replaceCurrentFragmentWithoutAddingToBackStack(Fragment newFragment) {
        latestBackStackFragment = getSupportFragmentManager().findFragmentById(R.id.one);
        Fragments.replaceFragment(this, R.id.one, newFragment);
        updateActionBarTitle(newFragment);
    }

    @Override
    public Fragment getLatestBackStackFragment() {
        return latestBackStackFragment;
    }

    @Override
    public void replaceFragment(int navigationLevel) {

    }

    @Override
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.one);
    }
}
