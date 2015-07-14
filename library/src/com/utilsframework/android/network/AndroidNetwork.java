package com.utilsframework.android.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.utilsframework.android.BuildConfig;
import com.utilsframework.android.view.GuiUtilities;

/**
 * Created by CM on 7/14/2015.
 */
public class AndroidNetwork {
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }

        NetworkInfo activeInfo = connectivity.getActiveNetworkInfo();
        if (activeInfo == null) {
            return false;
        }

        return activeInfo.isAvailable() && activeInfo.isConnected();
    }
}
