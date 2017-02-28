package com.utilsframework.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.utils.framework.Lists;
import com.utilsframework.android.WeakUiLoopEvent;
import com.utilsframework.android.view.GuiUtilities;

import java.util.*;
import com.utils.framework.Objects;

/**
 * User: Tikhonenko.S
 * Date: 06.08.14
 * Time: 20:03
 */
public abstract class ViewArrayAdapter<Element, ViewHolder> extends BaseAdapter {
    protected static final int HEADER_VIEW_TYPE = 2;
    protected static final int NULL_VIEW_TYPE = 1;
    protected static final int NORMAL_VIEW_TYPE = 0;
    protected static final int VIEW_TYPES_COUNT = 3;
    private static final int ELEMENT_KEY = "ELEMENT_KEY".hashCode();
    private static final int POSITION_KEY = "POSITION_KEY".hashCode();

    public enum AutoUpdateMode {
        UPDATE_NOTHING, UPDATE_NULL, UPDATE_ALL
    }

    private List<Element> elements;
    private OnNullElementReceived<Element> onNullElementReceivedListener;

    private LayoutInflater inflater;
    private WeakUiLoopEvent<ViewArrayAdapter> nullItemsUpdater;
    private WeakUiLoopEvent<ViewArrayAdapter> itemsUpdater;
    private Set<Integer> nullItemsPositions;
    private View header;
    private boolean reverse;
    private View emptyView;
    private AutoUpdateMode autoUpdateMode = AutoUpdateMode.UPDATE_NOTHING;
    private ViewGroup viewGroup;

    public List<Element> getElements() {
        return elements;
    }

    private int getElementsCount() {
        if(elements == null){
            return 0;
        }

        return elements.size();
    }

    @Override
    public int getCount() {
        int elementsCount = getElementsCount();
        if(header != null){
            elementsCount++;
        }

        return elementsCount;
    }

    @Override
    public Object getItem(int position) {
        if(header != null) {
            position--;
        }

        if(position < 0){
            return header;
        }

        return getElement(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(header != null){
            position--;
            if(position < 0){
                return HEADER_VIEW_TYPE;
            }
        }

        Element element = getElement(position);
        if(element == null){
            return NULL_VIEW_TYPE;
        } else {
            return getItemViewTypeOfElement(element);
        }
    }

    protected int getItemViewTypeOfElement(Element element) {
        return NORMAL_VIEW_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPES_COUNT;
    }

    protected void reuseNullView(final int position, View convertView){

    }

    protected void onViewCreated(final int position, View convertView, Element element, ViewHolder holder) {

    }

    @Override
    public View getView(int position, View convertView, final ViewGroup viewGroup) {
        this.viewGroup = viewGroup;

        if(reverse){
            position = getCount() - position - 1;
        }

        if(header != null){
            position--;
        }

        if(position < 0){
            return header;
        }

        final Element element = getElement(position);

        if(element == null){
            if(onNullElementReceivedListener != null){
                onNullElementReceivedListener.onNull(this, Collections.unmodifiableList(elements), position);
            }

            if(convertView != null && getElementOfView(convertView) != null){
                convertView = null;
            }

            if(nullItemsUpdater == null && autoUpdateMode == AutoUpdateMode.UPDATE_NULL){
                nullItemsPositions = new HashSet<Integer>();
                nullItemsUpdater = new WeakUiLoopEvent<ViewArrayAdapter>(this);
                nullItemsUpdater.run(new NullItemsUpdater(nullItemsUpdater));
            }

            if (nullItemsPositions != null) {
                nullItemsPositions.add(position);
            }

            return getNullView(position, convertView);
        }

        ViewHolder viewHolder = null;
        if(convertView != null){
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(viewHolder == null){
            int itemViewType = getItemViewType(position);
            convertView = inflater.inflate(getRootLayoutId(itemViewType),null);
            viewHolder = createViewHolder(convertView, itemViewType);
            convertView.setTag(viewHolder);
            onViewCreated(position, convertView, element, viewHolder);
        }

        convertView.setTag(ELEMENT_KEY, element);
        convertView.setTag(POSITION_KEY, position);
        reuseView(element, viewHolder, position, convertView);

        return convertView;
    }

    private static class NullItemsUpdater implements Runnable {
        private final WeakUiLoopEvent<ViewArrayAdapter> nullItemsUpdater;

        public NullItemsUpdater(WeakUiLoopEvent<ViewArrayAdapter> nullItemsUpdater) {
            this.nullItemsUpdater = nullItemsUpdater;
        }

        @Override
        public void run() {
            nullItemsUpdater.get().updateNullItems();
        }
    }

    private void updateNullItems() {
        boolean shouldCallNotifyDataSetChanged = false;
        Iterator<Integer> iterator = nullItemsPositions.iterator();
        while (iterator.hasNext()) {
            int position = iterator.next();
            if (getElement(position) != null) {
                shouldCallNotifyDataSetChanged = true;
                iterator.remove();
            }
        }

        if (shouldCallNotifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    public View getHeader() {
        return header;
    }

    /* Use ListView setHeader instead */
    @Deprecated
    public void setHeader(View header) {
        this.header = header;
        notifyDataSetChanged();
    }

    private View getNullView(int position, View convertView) {
        if(convertView == null){
            convertView = inflater.inflate(getNullLayoutId(),null);
        }

        reuseNullView(position, convertView);
        return convertView;
    }

    protected abstract int getRootLayoutId(int viewType);
    protected int getNullLayoutId(){
        throw new NullPointerException("null elements are not allowed");
    }
    protected abstract ViewHolder createViewHolder(View view, int itemViewType);
    protected abstract void reuseView(Element element, ViewHolder viewHolder, int position, View view);

    public final Element getElement(int index){
        return elements.get(index);
    }

    public Element getElementByViewPosition(int position) {
        if(header == null) {
            return getElement(position);
        } else {
            if(position == 0){
                return null;
            } else {
                return getElement(position - 1);
            }
        }
    }

    public void setElements(List<Element> elements){
        this.elements = elements;
        notifyDataSetChanged();
    }

    private void internalAddElement(Element element){
        elements.add(element);
    }

    public final Element getElementOfView(View view) {
        return (Element) view.getTag(ELEMENT_KEY);
    }

    public final Integer getPositionOfView(View view) {
        return (Integer) view.getTag(POSITION_KEY);
    }

    public final void addElement(Element element){
        internalAddElement(element);
        notifyDataSetChanged();
    }

    public void addElementToFront(Element element) {
        elements.add(0, element);
    }

    public void addElements(Iterable<Element> elements){
        if (this.elements == null) {
            setElements(Lists.fromIterable(elements));
            return;
        }

        for(Element i : elements){
            internalAddElement(i);
        }
        notifyDataSetChanged();
    }

    public ViewArrayAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    public ViewArrayAdapter(Context context, View header) {
        this(context);
        this.header = header;
    }

    public OnNullElementReceived<Element> getOnNullElementReceivedListener() {
        return onNullElementReceivedListener;
    }

    public void setOnNullElementReceivedListener(OnNullElementReceived<Element> onNullElementReceivedListener) {
        this.onNullElementReceivedListener = onNullElementReceivedListener;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public AutoUpdateMode getAutoUpdateMode() {
        return autoUpdateMode;
    }

    public void setAutoUpdateMode(AutoUpdateMode autoUpdateMode) {
        if (autoUpdateMode != AutoUpdateMode.UPDATE_NULL) {
            if (nullItemsUpdater != null) {
                nullItemsUpdater.stop();
                nullItemsUpdater = null;
                nullItemsPositions = null;
                notifyDataSetChanged();
            }
        }

        if (autoUpdateMode == AutoUpdateMode.UPDATE_ALL) {
            itemsUpdater = new WeakUiLoopEvent<ViewArrayAdapter>(this);
            itemsUpdater.run(new AllItemsUpdater(itemsUpdater));
        } else if(itemsUpdater != null) {
            itemsUpdater.stop();
            itemsUpdater = null;
        }

        this.autoUpdateMode = autoUpdateMode;
    }

    private static class AllItemsUpdater implements Runnable {
        private final WeakUiLoopEvent<ViewArrayAdapter> allItemsUpdater;

        public AllItemsUpdater(WeakUiLoopEvent<ViewArrayAdapter> allItemsUpdater) {
            this.allItemsUpdater = allItemsUpdater;
        }

        @Override
        public void run() {
            allItemsUpdater.get().updateAllItems();
        }
    }

    private void updateAllItems() {
        List<View> children = GuiUtilities.getChildrenAsListNonCopy(viewGroup);
        for (View view : children) {
            Integer position = getPositionOfView(view);
            if (position != null) {
                Element newElement = getElement(position);
                Element oldElement = getElementOfView(view);

                if (!Objects.equals(newElement, oldElement)) {
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != NULL_VIEW_TYPE;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public void removeItemAt(int index) {
        elements.remove(index);
        notifyDataSetChanged();
    }

    public void removeItem(Element item) {
        elements.remove(item);
        notifyDataSetChanged();
    }
}
