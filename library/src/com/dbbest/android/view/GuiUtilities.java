package com.dbbest.android.view;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import com.dbbest.android.BuildConfig;
import com.dbbest.framework.CollectionUtils;
import com.dbbest.framework.Predicate;
import com.dbbest.framework.predicates.InstanceOfPredicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public static List<View> getAllChildrenRecursive(View view){
        if(!(view instanceof ViewGroup)){
            return new ArrayList<View>();
        }

        List<View> result = new ArrayList<View>();
        ViewGroup viewGroup = (ViewGroup)view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            result.add(child);
            result.addAll(getAllChildrenRecursive(child));
        }

        return result;
    }

    public static List<View> getAllChildrenRecursive(Activity activity){
        return getAllChildrenRecursive(activity.getWindow().getDecorView().getRootView());
    }

    public static List<View> getAllChildrenRecursive(Activity activity, Predicate<View> predicate){
        List<View> views = getAllChildrenRecursive(activity);
        return CollectionUtils.findAll(views, predicate);
    }

    public static List<View> getAllChildrenRecursive(Activity activity, Class<? extends View> aClass){
        InstanceOfPredicate<View> predicate = new InstanceOfPredicate<View>(aClass);
        return getAllChildrenRecursive(activity, predicate);
    }

    public static List<View> getNonViewGroupChildrenRecursive(View view){
        List<View> children = getAllChildrenRecursive(view);
        CollectionUtils.removeAllWithType(children, ViewGroup.class);
        return children;
    }

    public static void setVisibility(Iterable<View> views, int visibility){
        for(View view : views){
            view.setVisibility(visibility);
        }
    }

    public static void setVisibility(View[] views, int visibility){
        setVisibility(Arrays.asList(views), visibility);
    }

    public static View getContentView(Activity activity){
        return activity.getWindow().getDecorView().getRootView();
    }
}
