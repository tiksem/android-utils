package com.utilsframework.android.adapters;

import android.content.Context;
import android.location.Address;
import android.view.View;
import android.widget.TextView;
import com.utils.framework.strings.Strings;
import com.utilsframework.android.R;

import java.util.AbstractList;

/**
 * Created by stykhonenko on 13.10.15.
 */
public class GoogleMapsSuggestionsAdapter extends SuggestionsAdapter<Address, GoogleMapsSuggestionHolder> {
    public GoogleMapsSuggestionsAdapter(Context context) {
        setViewArrayAdapter(new ViewArrayAdapter<Address, GoogleMapsSuggestionHolder>(context) {
            @Override
            protected int getRootLayoutId(int viewType) {
                return GoogleMapsSuggestionsAdapter.this.getRootLayoutId();
            }

            @Override
            protected GoogleMapsSuggestionHolder createViewHolder(View view) {
                GoogleMapsSuggestionHolder holder = new GoogleMapsSuggestionHolder();
                holder.header = (TextView) view.findViewById(getHeaderId());
                holder.text = (TextView) view.findViewById(getTextId());

                return holder;
            }

            @Override
            protected void reuseView(final Address address, GoogleMapsSuggestionHolder holder,
                                     int position, View view) {
                String header = address.getAddressLine(0);
                holder.header.setText(header);

                StringBuilder text = Strings.join(", ", new AbstractList<CharSequence>() {
                    @Override
                    public CharSequence get(int location) {
                        return address.getAddressLine(location + 1);
                    }

                    @Override
                    public int size() {
                        return address.getMaxAddressLineIndex();
                    }
                });
                holder.text.setText(text);
            }
        });
    }

    protected int getRootLayoutId() {
        return R.layout.google_maps_suggestion;
    }

    protected int getHeaderId() {
        return R.id.header;
    }

    protected int getTextId() {
        return R.id.text;
    }
}
