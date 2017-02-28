package com.utilsframework.android.network;

import com.utils.framework.CollectionUtils;
import com.utils.framework.KeyProvider;
import com.utils.framework.OnError;
import com.utils.framework.Transformer;
import com.utils.framework.collections.LazyLoadingList;
import com.utils.framework.collections.OnLoadingFinished;
import com.utilsframework.android.network.retrofit.CallProvider;
import com.utilsframework.android.network.retrofit.RetrofitRequestManager;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;

public abstract class MultipleCallsOnePageRetrofitLazyLoadingList<T, RequestType> extends LazyLoadingList<T> {
    private RetrofitRequestManager requestManager;

    public MultipleCallsOnePageRetrofitLazyLoadingList(RetrofitRequestManager requestManager) {
        this.requestManager = requestManager;
    }

    protected abstract List<Call<List<RequestType>>> createCalls();

    @Override
    public void getElementsOfPage(int pageNumber,
                                  final OnLoadingFinished<T> onPageLoadingFinished, final OnError onError) {
        final List<T> result = new ArrayList<>();
        List<CallProvider> callProviders = CollectionUtils.transform(createCalls(),
                new Transformer<Call<List<RequestType>>, CallProvider>() {
            @Override
            public CallProvider<List<RequestType>> get(final Call<List<RequestType>> call) {
                return new CallProvider<List<RequestType>>() {
                    @Override
                    public Call<List<RequestType>> getCall() {
                        return call;
                    }

                    @Override
                    public void onSuccess(final List<RequestType> list) {
                        result.addAll(transform(list));
                    }
                };
            }
        });
        requestManager.executeMultipleCalls(callProviders, new RequestListener<List, Throwable>() {
            @Override
            public void onSuccess(List list) {
                onPageLoadingFinished.onLoadingFinished(result, true);
            }

            @Override
            public void onError(Throwable e) {
                onError.onError(e);
            }

            @Override
            public void onCanceled() {
                onPageLoadingFinished.onLoadingFinished(CANCELLED_PAGE, true);
            }
        }, CancelStrategy.INTERRUPT);
    }

    protected abstract Collection<? extends T> transform(List<RequestType> list);
}
