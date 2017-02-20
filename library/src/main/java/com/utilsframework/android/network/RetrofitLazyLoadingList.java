package com.utilsframework.android.network;

import com.utils.framework.OnError;
import com.utils.framework.collections.LazyLoadingList;
import com.utils.framework.collections.OnLoadingFinished;
import com.utils.framework.collections.UniqueLazyLoadingList;
import com.utilsframework.android.network.retrofit.RetrofitRequestManager;

import java.util.List;

import retrofit2.Call;

public abstract class RetrofitLazyLoadingList<T> extends UniqueLazyLoadingList<T> {
    private RetrofitRequestManager requestManager;
    private int itemsPerPage;

    public RetrofitLazyLoadingList(RetrofitRequestManager requestManager, int itemsPerPage) {
        this.requestManager = requestManager;
        this.itemsPerPage = itemsPerPage;
    }

    @Override
    public void getElementsOfPage(int pageNumber, final OnLoadingFinished<T> onPageLoadingFinished,
                                  final OnError onError) {
        Call<List<T>> call = createLoadPageCall(itemsPerPage);
        requestManager.executeCall(call, new RequestListener<List<T>, Throwable>() {
            @Override
            public void onCanceled() {
                onPageLoadingFinished.onLoadingFinished(CANCELLED_PAGE, false);
            }

            @Override
            public void onSuccess(List<T> list) {
                onPageLoadingFinished.onLoadingFinished(list, isLastPage(list, itemsPerPage));
            }

            @Override
            public void onError(Throwable e) {
                onError.onError(e);
            }
        }, CancelStrategy.INTERRUPT);
    }

    protected boolean isLastPage(List<T> list, int itemsPerPage) {
        return list.size() < itemsPerPage;
    }

    protected abstract Call<List<T>> createLoadPageCall(int itemsPerPage);
}
