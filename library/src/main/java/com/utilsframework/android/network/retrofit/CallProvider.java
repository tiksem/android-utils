package com.utilsframework.android.network.retrofit;


import com.utilsframework.android.threading.OnSuccess;

import retrofit2.Call;
import retrofit2.Response;

public abstract class CallProvider<T> implements OnSuccess<T> {
    public abstract Call<T> getCall();

    @Override
    public void onSuccess(T result) {}

    public void onSuccess(Response<T> result) {}
}
