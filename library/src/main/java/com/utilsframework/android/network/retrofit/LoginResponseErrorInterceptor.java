package com.utilsframework.android.network.retrofit;

import retrofit2.Response;

public abstract class LoginResponseErrorInterceptor implements
        RetrofitRequestManagerResponseErrorInterceptor {
    @Override
    public boolean onResponseError(Response response) {
        if (response.code() == 401) {
            onLoginRequired();
            return true;
        }

        return false;
    }

    protected abstract void onLoginRequired();
}
