package com.utilsframework.android.navdrawer;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewStub;
import com.utilsframework.android.view.LayoutRadioButtonGroup;

/**
 * Created by stykhonenko on 28.10.15.
 * Not implemented, don't use
 */
public class LayoutRadioButtonGroupTabsAdapter implements TabsAdapter {
    private final LayoutRadioButtonGroup radioButtonGroup;

    private LayoutRadioButtonGroupTabsAdapter(Activity activity,
                                              int viewStubId,
                                              int layoutId,
                                              int idInLayout) {
        ViewStub viewStub = (ViewStub) activity.findViewById(viewStubId);
        viewStub.setLayoutResource(layoutId);
        radioButtonGroup = (LayoutRadioButtonGroup) viewStub.inflate().findViewById(idInLayout);
    }

    public static LayoutRadioButtonGroupTabsAdapter fromViewStub(Activity activity,
                                                int viewStubId,
                                                int layoutId,
                                                int idInLayout) {
        return new LayoutRadioButtonGroupTabsAdapter(activity, viewStubId, layoutId, idInLayout);
    }

    private class TabHolder implements Tab {
        LayoutRadioButtonGroup.LayoutRadioButton radioButton;

        public TabHolder(LayoutRadioButtonGroup.LayoutRadioButton radioButton) {
            this.radioButton = radioButton;
        }

        @Override
        public void setText(CharSequence text) {

        }

        @Override
        public void setText(int id) {

        }

        @Override
        public void setIcon(int resourceId) {

        }

        @Override
        public int getIndex() {
            return 0;
        }

        @Override
        public Object getTabHandler() {
            return radioButton;
        }
    }

    @Override
    public void setOnTabSelected(final OnTabSelected listener) {
        radioButtonGroup.setOnSelectedChangedListener(new LayoutRadioButtonGroup.OnSelectedChanged() {
            @Override
            public void onSelectedChanged(boolean fromUser, LayoutRadioButtonGroup.LayoutRadioButton item,
                                          LayoutRadioButtonGroup.LayoutRadioButton old) {
                listener.onTabSelected(new TabHolder(item));
            }
        });
    }

    @Override
    public Tab newTab(boolean isSelected) {
        return null;
    }

    @Override
    public void removeAllTabs() {

    }

    @Override
    public void selectTab(int index) {

    }

    @Override
    public View getView() {
        return null;
    }
}
