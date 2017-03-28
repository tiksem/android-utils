package com.utilsframework.android.network.retrofit;

import com.utilsframework.android.network.CancelStrategy;
import com.utilsframework.android.network.RequestListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public interface RetrofitRequestExecutor {
    <Result> void executeCall(Call<Result> call, RequestListener<Result, Throwable> requestListener,
                              CancelStrategy cancelStrategy);
    <Result> void executeCallWithResponseCallBack(Call<Result> call,
                                                  RequestListener<Response<Result>, Throwable>
                                                          requestListener,
                                                  CancelStrategy cancelStrategy);
    void executeMultipleCalls(List<CallProvider> calls,
                              RequestListener<List, Throwable> requestListener,
                              CancelStrategy cancelStrategy);
}
