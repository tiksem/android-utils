package com.utilsframework.android.navdrawer;

import android.view.View;

public abstract class BaseMenuLayoutAdapter implements MenuLayoutAdapter {
    private Listener listener;

    public void setListener(final Listener listener) {
        this.listener = listener;
    }

    public Listener getListener() {
        return listener;
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    public void registerItemAsSelectable(View item) {
        final int id = item.getId();
        if (id == View.NO_ID) {
            throw new IllegalArgumentException("Item without id cannot be selectable");
        }

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemSelected(v.getId());
                }
            }
        });
    }
}
