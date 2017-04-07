package com.utilsframework.android.navdrawer;

import android.view.View;

import com.utilsframework.android.view.LayoutRadioButtonGroup;

public class LayoutRadioButtonGroupMenuAdapter extends BaseMenuLayoutAdapter {
    private LayoutRadioButtonGroup radioButtonGroup;

    public LayoutRadioButtonGroupMenuAdapter(LayoutRadioButtonGroup radioButtonGroup) {
        this.radioButtonGroup = radioButtonGroup;
    }

    @Override
    public void setListener(final Listener listener) {
        super.setListener(listener);
        radioButtonGroup.setOnSelectedChangedListener(new LayoutRadioButtonGroup.OnSelectedChanged() {
            @Override
            public void onSelectedChanged(boolean fromUser,
                                          LayoutRadioButtonGroup.LayoutRadioButton item,
                                          LayoutRadioButtonGroup.LayoutRadioButton old) {
                int id = item.getId();
                if (id == View.NO_ID) {
                    throw new IllegalStateException("LayoutRadioButton must have id in " +
                            "order to be selectable");
                }

                listener.onItemSelected(id);
            }
        });
    }

    @Override
    public void applySelectItemVisualStyle(int id) {

    }
}
