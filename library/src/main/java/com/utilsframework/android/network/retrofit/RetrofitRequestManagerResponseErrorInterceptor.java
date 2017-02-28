package com.utilsframework.android.network.retrofit;

import retrofit2.Response;

public interface RetrofitRequestManagerResponseErrorInterceptor {
    // return true if you don't want to call the user's listener, false otherwise
    boolean onResponseError(Response response);
}
