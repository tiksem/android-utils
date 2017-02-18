package com.utilsframework.android.network.retrofit;

import com.utilsframework.android.network.CancelStrategy;
import com.utilsframework.android.network.RequestListener;

import retrofit2.Call;

public interface RetrofitRequestExecutor {
    <Result> void executeCall(Call<Result> call, RequestListener<Result, Throwable> requestListener,
                              CancelStrategy cancelStrategy);
}
