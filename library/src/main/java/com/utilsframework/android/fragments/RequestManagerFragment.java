package com.utilsframework.android.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.utilsframework.android.network.RequestManager;

/**
 * Created by stykhonenko on 12.10.15.
 */
public abstract class RequestManagerFragment<RequestManagerImpl extends RequestManager> extends Fragment {
    private RequestManagerImpl requestManager;

    public RequestManagerImpl getRequestManager() {
        return requestManager;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        requestManager = obtainRequestManager();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        requestManager.cancelAll();
    }

    protected abstract RequestManagerImpl obtainRequestManager();
}
