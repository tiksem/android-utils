package com.dbbest.android.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.dbbest.android.BuildConfig;

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

}
