package com.utilsframework.android.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.utils.framework.Lists;
import com.utilsframework.android.R;

import java.util.List;

public abstract class SpinnerAdapter<Element> extends ViewArrayAdapter<Element, TextView> {
    public SpinnerAdapter(Context context) {
        super(context);
    }

    @Override
    protected TextView createViewHolder(View view) {
        return (TextView) view;
    }

    @Override
    protected void reuseView(Element element, TextView textView, int position, View view) {
        textView.setText(getTextOfElement(element));
    }

    @Override
    protected void reuseNullView(int position, View convertView) {
        ((TextView)convertView).setText(getHint());
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    protected int getNullLayoutId() {
        return getRootLayoutId(0);
    }

    public abstract int getHint();

    public String getTextOfElement(Element element) {
        return element.toString();
    }

    @Override
    public void setElements(List<Element> elements) {
        super.setElements(Lists.listWithAddingElementToFront(elements, null));
    }

    @Override
    protected int getRootLayoutId(int viewType) {
        return R.layout.spinner_item;
    }
}
