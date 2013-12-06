package com.dbbest.android.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import android.view.View;
import com.dbbest.android.BuildConfig;

import java.util.Arrays;

/**
 * Created by Tikhonenko.S on 19.09.13.
 */
public class GuiUtilities {

	private static final String TAG = "GuiUtilities";

	public final static boolean isOnline(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.w(TAG, "couldn't get connectivity manager");
			return false;
		}

		NetworkInfo activeInfo = connectivity.getActiveNetworkInfo();
		if (activeInfo == null) {
			if (BuildConfig.DEBUG) {
				Log.v(TAG, "network is not available");
			}
			return false;
		}

		return activeInfo.isAvailable() && activeInfo.isConnected();
	}

    public static void setVisibility(Iterable<View> views, int visibility){
        for(View view : views){
            view.setVisibility(visibility);
        }
    }

    public static void setVisibility(View[] views, int visibility){
        setVisibility(Arrays.asList(views), visibility);
    }
}
