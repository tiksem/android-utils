package com.utilsframework.android.view;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.utils.framework.collections.map.ListValuesMultiMap;
import com.utils.framework.collections.map.MultiMap;
import com.utilsframework.android.fragments.Fragments;
import com.utilsframework.android.threading.OnComplete;
import com.utilsframework.android.threading.Tasks;

import java.util.HashSet;

/**
 * Created by CM on 2/22/2015.
 */
public class NextPrevCyclingViewPager extends ExtendedViewPager {
    private static final int FIRST_ITEM_INDEX = Integer.MAX_VALUE / 2;

    private Adapter nextPrevAdapter;
    private MultiMap<Integer, Runnable> loadingFragments = new ListValuesMultiMap<Integer, Runnable>();

    public NextPrevCyclingViewPager(Context context) {
        super(context);
        init();
    }

    public NextPrevCyclingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public interface Adapter<T extends Fragment> {
        T createFirstFragment(OnComplete onComplete);
        T createNextFragment(T currentFragment, OnComplete onComplete);
        T createPrevFragment(T currentFragment, OnComplete onComplete);
    }

    public FragmentManager getFragmentManager() {
        Activity activity = (Activity) getContext();
        return activity.getFragmentManager();
    }

    public Fragment getCurrentFragment() {
        int currentItem = getCurrentItem();
        return getFragmentManager().findFragmentById(currentItem);
    }

    private void notifyFragmentLoaded(int position) {
        if(position == getCurrentItem()){
            setPagingEnabled(true);
        }

        Tasks.executeAndClearQueue(loadingFragments.getValues(position));
        loadingFragments.removeAll(position);
    }

    private void addFragment(int position, Fragment fragment) {
        getFragmentManager().beginTransaction().add(position, fragment).commit();
    }

    private void init() {
        super.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return o == view;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                Fragment fragment = null;

                final FrameLayout frameLayout = new FrameLayout(getContext());
                frameLayout.setId(position);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                frameLayout.setLayoutParams(layoutParams);
                container.addView(frameLayout);

                final OnComplete onComplete = new OnComplete() {
                    @Override
                    public void onFinish() {
                        notifyFragmentLoaded(position);
                    }
                };
                loadingFragments.put(position, new Runnable() {
                    @Override
                    public void run() {

                    }
                });

                if (position == FIRST_ITEM_INDEX) {
                    fragment = nextPrevAdapter.createFirstFragment(onComplete);
                } else {
                    final int currentItem = getCurrentItem();
                    final Fragment currentFragment = getCurrentFragment();
                    if(currentFragment == null || loadingFragments.containsKey(currentItem)){
                        loadingFragments.put(currentItem, new Runnable() {
                            @Override
                            public void run() {
                                Fragment currentFragment = getFragmentManager().findFragmentById(currentItem);
                                Fragment fragment = createFragment(position, onComplete, currentItem, currentFragment);
                                addFragment(position, fragment);
                            }
                        });
                    } else {
                        fragment = createFragment(position, onComplete, currentItem, currentFragment);
                    }
                }

                if (fragment != null) {
                    addFragment(position, fragment);
                }

                return frameLayout;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Fragments.removeFragmentWithId(getFragmentManager(), position);
                container.removeView((View) object);
            }
        });

        setCurrentItem(FIRST_ITEM_INDEX, false);

        super.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (loadingFragments.containsKey(position)) {
                    setPagingEnabled(false);
                }
            }
        });
    }

    private Fragment createFragment(int position, OnComplete onComplete, int currentItem, Fragment currentFragment) {
        Fragment fragment;
        if(currentItem < position){
            fragment = nextPrevAdapter.createNextFragment(currentFragment, onComplete);
        } else {
            fragment = nextPrevAdapter.createPrevFragment(currentFragment, onComplete);
        }
        return fragment;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        throw new UnsupportedOperationException("Use setNextPrevAdapter");
    }

    public Adapter getNextPrevAdapter() {
        return nextPrevAdapter;
    }

    public void setNextPrevAdapter(Adapter nextPrevAdapter) {
        this.nextPrevAdapter = nextPrevAdapter;
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        throw new UnsupportedOperationException();
    }
}
