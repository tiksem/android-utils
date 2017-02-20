package com.utilsframework.android.network;

import com.utilsframework.android.network.retrofit.RetrofitRequestManager;

import java.util.List;

import retrofit2.Call;

public abstract class RetrofitOnePageLazyLoadingList<T> extends RetrofitLazyLoadingList<T> {
    public RetrofitOnePageLazyLoadingList(RetrofitRequestManager requestManager) {
        super(requestManager, -1);
    }

    @Override
    protected final Call<List<T>> createLoadPageCall(int itemsPerPage) {
        return createLoadPageCall();
    }

    protected abstract Call<List<T>> createLoadPageCall();

    @Override
    protected final boolean isLastPage(List<T> list, int itemsPerPage) {
        return true;
    }
}
