package com.utilsframework.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.utils.framework.Lists;

import java.util.Collections;
import java.util.List;

public abstract class RecyclerViewListAdapter<Element, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements ListAdapter<Element>, AdapterItemClickListenerHolder {
    private List<Element> items;
    private ItemClickListener itemClickListener;

    public abstract int getItemLayoutId(int viewType);
    protected abstract VH onCreateViewHolder(View view, int viewType);
    protected abstract void onBindViewHolder(VH holder, int position, Element item);

    @Override
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), getItemLayoutId(viewType),
                null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemView.setLayoutParams(lp);
        return onCreateViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemCLicked(position);
                }
            });
        }

        onBindViewHolder(holder, position, items.get(position));
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }

        return items.size();
    }

    @Override
    public List<Element> getElements() {
        return items;
    }

    @Override
    public void setElements(List<Element> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public final int getItemViewType(int position) {
        return getItemViewType(items.get(position), position);
    }

    protected int getItemViewType(Element item, int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void removeItemAt(int index) {
        items.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public void removeItem(Element item) {
        int index = items.indexOf(item);
        removeItemAt(index);
    }

    public void swapItems(int position1, int position2) {
        Collections.swap(items, position1, position2);
        notifyItemMoved(position1, position2);
    }

    public void addItem(Element item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void insertItemAt(Element item, int index) {
        items.add(index, item);
        notifyItemInserted(index);
    }

    public void replaceItemAt(Element item, int index) {
        items.set(index, item);
        notifyItemChanged(index, item);
    }

    public void moveItem(int fromPosition, int toPosition) {
        Element item = items.remove(fromPosition);
        int size = items.size();
        if (toPosition > size) {
            toPosition = size;
        }

        items.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void moveItems(int fromPositionA, int fromPositionB, int toPosition) {
        if (toPosition >= fromPositionA && toPosition <= fromPositionB) {
            throw new IllegalArgumentException("toPosition >= fromPositionA && " +
                    "toPosition <= fromPositionB");
        }

        if (toPosition >= items.size()) {
            throw new IllegalArgumentException("toPosition >= items.size()");
        }

        if (toPosition < 0) {
            throw new IllegalArgumentException("toPosition < 0");
        }

        List<Element> removedItems = removeItemsGetRemoved(fromPositionA, fromPositionB);

        if (toPosition > fromPositionB) {
            toPosition -= (fromPositionB - fromPositionA);
        }

        getElements().addAll(toPosition, removedItems);
        notifyItemRangeInserted(toPosition, removedItems.size());
    }

    public List<Element> removeItemsGetRemoved(int a, int b) {
        List<Element> removedItems = Lists.removeRangeGetRemovedItems(getElements(), a, b);
        notifyItemRangeRemoved(a, b - a + 1);
        return removedItems;
    }

    @Override
    public Element getElement(int index) {
        return items.get(index);
    }

    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void notifyItemChanged(Element item) {
        int index = getElements().indexOf(item);
        notifyItemChanged(index);
    }
}
