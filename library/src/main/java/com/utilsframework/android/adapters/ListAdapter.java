package com.utilsframework.android.adapters;

import java.util.List;

public interface ListAdapter<Element> {
    List<Element> getElements();
    void setElements(List<Element> elements);
    Element getElement(int index);
    void notifyDataSetChanged();
    int getCount();
    void notifyItemRemoved(int position);
    void removeItemAt(int index);
    void removeItem(Element item);
    void notifyItemChanged(int position);
    void notifyItemChanged(Element item);
}
