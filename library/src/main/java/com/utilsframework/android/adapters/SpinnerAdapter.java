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
        int textColor = getTextColor();
        if (textColor != 0) {
            textView.setTextColor(textView.getResources().getColor(textColor));
        }
    }

    @Override
    protected void reuseNullView(int position, View convertView) {
        TextView textView = (TextView) convertView;
        textView.setText(getHintAsString(textView.getContext()));
        int hintTextColor = getHintTextColor();
        if (hintTextColor != 0) {
            textView.setTextColor(textView.getResources().getColor(hintTextColor));
        }
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    public int getHintTextColor() {
        return 0;
    }

    public int getTextColor() {
        return 0;
    }

    @Override
    protected int getNullLayoutId() {
        return getRootLayoutId(0);
    }

    public int getHint() {
        return 0;
    }

    public String getHintAsString(Context context) {
        int hint = getHint();
        if (hint != 0) {
            return context.getString(hint);
        }

        return "No hint";
    }

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
