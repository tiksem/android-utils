package com.utilsframework.android.suggestions;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import com.utils.framework.suggestions.SuggestionsProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by stykhonenko on 13.10.15.
 */
public class GoogleMapsSuggestionsProvider implements SuggestionsProvider<Address> {
    private final Context context;
    private int count = 15;

    public GoogleMapsSuggestionsProvider(Context context, int count) {
        this(context);
        this.count = count;
    }

    public GoogleMapsSuggestionsProvider(Context context) {
        this.context = context;
    }

    @Override
    public List<Address> getSuggestions(String query) {
        List<Address> searchResults = new ArrayList<>();

        Geocoder geocoder = new Geocoder(context, context.getResources().getConfiguration().locale);
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(query, count);

            for (Address address : addresses) {
                if (address.getMaxAddressLineIndex() > 0) {
                    searchResults.add(address);
                }
            }
        } catch (IOException e) {
            return Collections.emptyList();
        }

        return searchResults;
    }
}
