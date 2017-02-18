package com.utilsframework.android.network.retrofit;

import com.utilsframework.android.network.CancelStrategy;
import com.utilsframework.android.network.RequestListener;

import retrofit2.Call;

public interface RetrofitRequestExecutor<ErrorData> {
    <Result> void executeCall(Call<Result> call, RequestListener<Result,
            RetrofitError<ErrorData>> requestListener, CancelStrategy cancelStrategy);
}
