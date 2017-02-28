package com.utilsframework.android.network.retrofit;

import retrofit2.Response;

public abstract class LoginResponseErrorInterceptor implements
        RetrofitRequestManagerResponseErrorInterceptor {
    @Override
    public Throwable onResponseError(Response response) {
        if (response.code() == 401) {
            onLoginRequired();
            return new LoginRequiredException();
        }

        return null;
    }

    protected abstract void onLoginRequired();
}
