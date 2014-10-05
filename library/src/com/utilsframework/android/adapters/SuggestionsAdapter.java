package com.utilsframework.android.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import com.utils.framework.suggestions.SuggestionsProvider;
import com.utils.framework.strings.Strings;

import java.util.List;

/**
 * Created by CM on 10/3/2014.
 */
public class SuggestionsAdapter<Element, ViewHolder> extends BaseAdapter implements Filterable {
    private ViewArrayAdapter<Element, ViewHolder> viewArrayAdapter;
    private SuggestionsProvider<Element> suggestionsProvider;
    private SuggestionsFilter suggestionsFilter = new SuggestionsFilter();
    private List<Element> lastSuggestedItems;

    private class SuggestionsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            List<Element> result = suggestionsProvider.getSuggestions(constraint.toString());
            filterResults.values = result;
            filterResults.count = result.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lastSuggestedItems = (List<Element>)results.values;
            viewArrayAdapter.setElements(lastSuggestedItems);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return getDisplayStringFromElement((Element) resultValue);
        }
    }

    public SuggestionsAdapter(ViewArrayAdapter<Element, ViewHolder> viewArrayAdapter,
                              SuggestionsProvider<Element> suggestionsProvider) {
        this.viewArrayAdapter = viewArrayAdapter;
        this.suggestionsProvider = suggestionsProvider;
    }

    @Override
    public int getCount() {
        return viewArrayAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return viewArrayAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return viewArrayAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return viewArrayAdapter.getView(position, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        return suggestionsFilter;
    }

    protected String getDisplayStringFromElement(Element element) {
        return Strings.toString(element);
    }

    public List<Element> getLastSuggestedItems() {
        return lastSuggestedItems;
    }
}
