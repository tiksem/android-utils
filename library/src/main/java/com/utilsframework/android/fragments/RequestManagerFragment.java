package com.utilsframework.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.utilsframework.android.network.retrofit.RetrofitRequestManager;

public abstract class RequestManagerFragment extends Fragment {
    private RetrofitRequestManager requestManager;

    public RetrofitRequestManager getRequestManager() {
        return requestManager;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestManager = obtainRequestManager();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        requestManager.cancelAll();
    }

    protected abstract RetrofitRequestManager obtainRequestManager();
}