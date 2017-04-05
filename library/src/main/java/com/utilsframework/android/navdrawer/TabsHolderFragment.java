package com.utilsframework.android.navdrawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.utilsframework.android.R;
import com.utilsframework.android.fragments.Fragments;

public class TabsHolderFragment extends Fragment {
    public static final String TAB_INDEX = "TAB_INDEX";
    private int tabIndex = -1;
    private TabsAdapter tabsAdapter;
    private ViewPager viewPager;
    private int currentSelectedItem = -1;
    private int navigationLevel = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabs_holder_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        final NavigationHandler navigationHandler = getNavigationHandler();
        if (currentSelectedItem == -1) {
            currentSelectedItem = navigationHandler.getCurrentSelectedItem();
        }
        if (navigationLevel == -1) {
            navigationLevel = navigationHandler.getNavigationLevel();
        }
        final FragmentFactory fragmentFactory = navigationHandler.getFragmentFactory();
        final int tabsCount = fragmentFactory.getTabsCount(currentSelectedItem,
                navigationLevel);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentFactory.
                        createFragmentBySelectedItem(currentSelectedItem, position,
                                navigationLevel);
            }

            @Override
            public int getCount() {
                return tabsCount;
            }
        });

        ViewStub tabsViewStub = (ViewStub) view.findViewById(R.id.tabsStub);
        tabsViewStub.setLayoutResource(navigationHandler.getTabsLayout());
        tabsAdapter = navigationHandler.createTabsAdapter(tabsViewStub.inflate());
        tabsAdapter.setOnTabSelected(new TabsAdapter.OnTabSelected() {
            @Override
            public void onTabSelected(TabsAdapter.Tab tab) {
                tabIndex = tab.getIndex();
                viewPager.setCurrentItem(tabIndex);
            }
        });

        int tabIndex = getCurrentSelectedTabIndex();
        for (int i = 0; i < tabsCount; i++) {
            TabsAdapter.Tab tab = tabsAdapter.newTab(i == tabIndex);
            fragmentFactory.initTab(currentSelectedItem, i, navigationLevel, tab);
        }
    }

    private NavigationHandler getNavigationHandler() {
        return ((NavigationHandlerProvider) getActivity()).getNavigationHandler();
    }

    public static TabsHolderFragment create(int tabIndex) {
        return Fragments.createFragmentWith1Arg(new TabsHolderFragment(), TAB_INDEX, tabIndex);
    }

    public int getCurrentSelectedTabIndex() {
        return tabIndex >= 0 ? tabIndex : getArguments().getInt(TAB_INDEX);
    }

    public void selectTab(int index) {
        if (viewPager.getChildCount() >= index) {
            throw new IllegalStateException("No such tab");
        }

        tabsAdapter.selectTab(index);
    }
}
