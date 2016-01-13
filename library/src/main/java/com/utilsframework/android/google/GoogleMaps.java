package com.utilsframework.android.google;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by CM on 7/9/2015.
 */
public class GoogleMaps {
    public static void startSearchAddressActivity(Context context, String address) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }
}
