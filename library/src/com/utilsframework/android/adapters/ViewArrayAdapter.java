package com.utilsframework.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.utilsframework.android.UiLoopEvent;
import com.utilsframework.android.view.GuiUtilities;

import java.util.*;

/**
 * User: Tikhonenko.S
 * Date: 06.08.14
 * Time: 20:03
 */
public abstract class ViewArrayAdapter<Element, ViewHolder> extends BaseAdapter {
    protected static final int NULL_VIEW_TYPE = 1;
    protected static final int NORMAL_VIEW_TYPE = 0;
    protected static final int VIEW_TYPES_COUNT = 2;
    private static final int ELEMENT_KEY = "ELEMENT_KEY".hashCode();
    private static final int POSITION_KEY = "POSITION_KEY".hashCode();

    private List<Element> elements;
    private OnNullElementReceived<Element> onNullElementReceivedListener;

    private LayoutInflater inflater;
    private ViewGroup parent;
    private UiLoopEvent nullItemsUpdater;
    private Set<Integer> nullItemsPositions;

    public List<Element> getElements() {
        return elements;
    }

    @Override
    public int getCount() {
        if(elements == null){
            return 0;
        }

        return elements.size();
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

    protected void onViewCreated(final int position, View convertView, Element element, ViewHolder holder) {

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        parent = viewGroup;
        final Element element = getElement(position);

        if(element == null){
            if(onNullElementReceivedListener != null){
                onNullElementReceivedListener.onNull(this, Collections.unmodifiableList(elements), position);
            }

            if(convertView != null && getElementOfView(convertView) != null){
                convertView = null;
            }

            if(nullItemsUpdater == null){
                nullItemsPositions = new HashSet<Integer>();
                nullItemsUpdater = new UiLoopEvent(this);
                nullItemsUpdater.run(new Runnable() {
                    @Override
                    public void run() {
                        boolean shouldCallNotifyDataSetChanged = false;
                        Iterator<Integer> iterator = nullItemsPositions.iterator();
                        while(iterator.hasNext()){
                            int position = iterator.next();
                            if(getElement(position) != null){
                                shouldCallNotifyDataSetChanged = true;
                                iterator.remove();
                            }
                        }

                        if(shouldCallNotifyDataSetChanged){
                            notifyDataSetChanged();
                        }
                    }
                });
            }

            nullItemsPositions.add(position);

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
            onViewCreated(position, convertView, element, viewHolder);
        }

        convertView.setTag(ELEMENT_KEY, element);
        convertView.setTag(POSITION_KEY, position);
        reuseView(element, viewHolder, position, convertView);

        return convertView;
    }

    private View getNullView(int position, View convertView) {
        if(convertView == null){
            convertView = inflater.inflate(getNullLayoutId(),null);
        }

        reuseNullView(position, convertView);
        return convertView;
    }

    protected abstract int getRootLayoutId();
    protected int getNullLayoutId(){
        throw new NullPointerException("null elements are not allowed");
    }
    protected abstract ViewHolder createViewHolder(View view);
    protected abstract void reuseView(Element element, ViewHolder viewHolder, int position, View view);

    public final Element getElement(int index){
        return elements.get(index);
    }

    public final void setElements(List<Element> elements){
        this.elements = elements;
        notifyDataSetChanged();
    }

    private void internalAddElement(Element element){
        elements.add(element);
    }

    public final Element getElementOfView(View view) {
        return (Element) view.getTag(ELEMENT_KEY);
    }

    protected final Integer getPositionOfView(View view) {
        return (Integer) view.getTag(POSITION_KEY);
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
