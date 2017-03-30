package com.utilsframework.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.utilsframework.android.R;

public abstract class RecyclerViewListAdapterWithNullItemsSupport<T,
        VH extends RecyclerView.ViewHolder>
        extends RecyclerViewListAdapter<T, RecyclerView.ViewHolder> {
    protected static final int NULL_VIEW_TYPE = 1;
    protected static final int NORMAL_VIEW_TYPE = 0;

    public abstract int getNormalItemLayoutId();

    @Override
    public int getItemLayoutId(int viewType) {
        if (viewType == NORMAL_VIEW_TYPE) {
            return getNormalItemLayoutId();
        } else if(viewType == NULL_VIEW_TYPE) {
            return getNullLayoutId();
        }

        throw new IllegalStateException("Unsupported view type");
    }

    @Override
    protected final RecyclerView.ViewHolder onCreateViewHolder(View view, int viewType) {
        if (viewType == NORMAL_VIEW_TYPE) {
            return onCreateItemViewHolder(view, viewType);
        } else {
            return new RecyclerView.ViewHolder(view) {};
        }
    }

    @Override
    protected final void onBindViewHolder(RecyclerView.ViewHolder holder, int position, T item) {
        if (item != null) {
            onBindItemViewHolder((VH) holder, position, item);
        }
    }

    protected abstract void onBindItemViewHolder(VH holder, int position, T item);
    protected abstract VH onCreateItemViewHolder(View view, int viewType);

    protected int getNullLayoutId() {
        return R.layout.navigation_list_null_item;
    }

    @Override
    protected int getItemViewType(T item, int position) {
        if (item != null) {
            return NORMAL_VIEW_TYPE;
        }

        return NULL_VIEW_TYPE;
    }
}
