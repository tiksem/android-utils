package com.utilsframework.android.navdrawer;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import com.utilsframework.android.view.LayoutRadioButtonGroup;

/**
 * Created by stykhonenko on 28.10.15.
 * Use this only if you want to create static tabs layout which would not change.
 */
public class LayoutRadioButtonGroupTabsAdapter implements TabsAdapter {
    private final LayoutRadioButtonGroup radioButtonGroup;

    private int newTabIndex = 0;

    private LayoutRadioButtonGroupTabsAdapter(Activity activity,
                                              int viewStubId,
                                              int layoutId,
                                              int idInLayout) {
        ViewStub viewStub = (ViewStub) activity.findViewById(viewStubId);
        viewStub.setLayoutResource(layoutId);
        radioButtonGroup = (LayoutRadioButtonGroup) viewStub.inflate().findViewById(idInLayout);
    }

    private LayoutRadioButtonGroupTabsAdapter(Context context,
                                              int layoutId,
                                              int inLayoutId) {
        View view = View.inflate(context, layoutId, null);
        radioButtonGroup = (LayoutRadioButtonGroup) view.findViewById(inLayoutId);
    }

    public static LayoutRadioButtonGroupTabsAdapter fromViewStub(Activity activity,
                                                int viewStubId,
                                                int layoutId,
                                                int idInLayout) {
        return new LayoutRadioButtonGroupTabsAdapter(activity, viewStubId, layoutId, idInLayout);
    }

    public static LayoutRadioButtonGroupTabsAdapter fromLayoutId(Context context, int layoutId, int inLayoutId) {
        return new LayoutRadioButtonGroupTabsAdapter(context, layoutId, inLayoutId);
    }

    private class TabHolder implements Tab {
        LayoutRadioButtonGroup.LayoutRadioButton radioButton;

        public TabHolder(LayoutRadioButtonGroup.LayoutRadioButton radioButton) {
            this.radioButton = radioButton;
        }

        @Override
        public void setText(CharSequence text) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setText(int id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setIcon(int resourceId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getIndex() {
            return radioButton.getIndex();
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
        LayoutRadioButtonGroup.LayoutRadioButton button = radioButtonGroup.getItemByIndex(newTabIndex++);
        if (isSelected) {
            button.select();
        }

        return new TabHolder(button);
    }

    @Override
    public void selectTab(int index) {
        radioButtonGroup.setSelectedItemIndex(index);
    }
}
