package com.utilsframework.android.fragments;

import com.utilsframework.android.network.AsyncRequestExecutionManager;
import com.utilsframework.android.network.RetrofitRequestExecutionManager;
import com.utilsframework.android.network.retrofit.RetrofitRequestManager;

public abstract class RetrofitLazyLoadingListFragment<T> extends LazyLoadingListFragment<T> {
    protected abstract RetrofitRequestManager obtainRequestManager();

    @Override
    public RetrofitRequestManager getRequestManager() {
        return (RetrofitRequestManager) super.getRequestManager();
    }

    @Override
    protected AsyncRequestExecutionManager createAsyncRequestExecutionManager() {
        return new RetrofitRequestExecutionManager(getRequestManager());
    }
}
