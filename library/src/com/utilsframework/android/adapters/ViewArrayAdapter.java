package com.utilsframework.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.AbstractList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 06.08.14
 * Time: 20:03
 */
public abstract class ViewArrayAdapter<Element, ViewHolder> extends BaseAdapter {
    protected static final int NULL_VIEW_TYPE = 1;
    protected static final int NORMAL_VIEW_TYPE = 0;
    protected static final int VIEW_TYPES_COUNT = 2;

    private List<Element> elements;
    private int maxRequestedPosition = -1;
    private int receivedNullElementPosition = -1;
    private OnNullElementReceived<Element> onNullElementReceivedListener;

    private LayoutInflater inflater;

    public List<Element> getElements() {
        return elements;
    }

    protected int getMaxDisplayedNullElementsCount(){
        return 1;
    }

    @Override
    public int getCount() {
        if(elements == null){
            return 0;
        }

        if(receivedNullElementPosition < 0){
            return elements.size();
        }
        else {
            return receivedNullElementPosition + getMaxDisplayedNullElementsCount();
        }
    }

    @Override
    public Object getItem(int position) {
        return getElement(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Element element = getElement(position);
        if(element == null){
            return NULL_VIEW_TYPE;
        }
        else {
            return NORMAL_VIEW_TYPE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPES_COUNT;
    }

    protected void reuseNullView(final int position, View convertView){

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final Element element = getElement(position);

        if(element == null){
            if(onNullElementReceivedListener != null){
                onNullElementReceivedListener.onNull(this, Collections.unmodifiableList(elements), position);
            }
            receivedNullElementPosition = position;
            return getNullView(position, convertView);
        }

        ViewHolder viewHolder = null;
        if(convertView != null){
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(viewHolder == null){
            convertView = inflater.inflate(getRootLayoutId(),null);
            viewHolder = createViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        if(position > maxRequestedPosition){
            onNewElementRequestedForDisplay(element, position, convertView, viewHolder);
            maxRequestedPosition = position;
        }

        reuseView(element,viewHolder,position);

        return convertView;
    }

    private View getNullView(int position, View convertView) {
        if(convertView == null){
            convertView = inflater.inflate(getNullLayoutId(),null);
        }

        reuseNullView(position, convertView);
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        receivedNullElementPosition = -1;
        super.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetInvalidated() {
        receivedNullElementPosition = -1;
        super.notifyDataSetInvalidated();
    }

    protected abstract int getRootLayoutId();
    protected int getNullLayoutId(){
        throw new NullPointerException("null elements are not allowed");
    }
    protected abstract ViewHolder createViewHolder(View view);
    protected abstract void reuseView(Element element, ViewHolder viewHolder, int position);

    public final Element getElement(int index){
        return elements.get(index);
    }

    public final void setElements(List<Element> elements){
        this.elements = elements;
        notifyDataSetChanged();
    }

    protected void onNewElementRequestedForDisplay(Element element, int position, View view,
                                                   ViewHolder viewHolder){

    }

    private void internalAddElement(Element element){
        elements.add(element);
    }

    public final void addElement(Element element){
        internalAddElement(element);
        notifyDataSetChanged();
    }

    public void addElements(Iterable<Element> elements){
        for(Element i : elements){
            internalAddElement(i);
        }
        notifyDataSetChanged();
    }

    public void onAdapterChanged(){
        setElements(Collections.<Element>emptyList());
    }

    public ViewArrayAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }

    public OnNullElementReceived<Element> getOnNullElementReceivedListener() {
        return onNullElementReceivedListener;
    }

    public void setOnNullElementReceivedListener(OnNullElementReceived<Element> onNullElementReceivedListener) {
        this.onNullElementReceivedListener = onNullElementReceivedListener;
    }
}
