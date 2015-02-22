package com.utilsframework.android.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.utilsframework.android.R;

/**
 * Created by CM on 2/21/2015.
 */
public class ViewPagerActivity extends Activity {
    private Adapter adapter;

    private class Adapter extends FragmentPagerAdapter {
        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_activity);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new Adapter(getFragmentManager());
        viewPager.setAdapter(adapter);
    }
}
