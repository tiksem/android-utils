package com.utilsframework.android.network.retrofit;

public class RetrofitError<Data> {
    private Throwable throwable;
    private Data data;

    public RetrofitError(Throwable throwable, Data data) {
        this.throwable = throwable;
        this.data = data;
    }

    public Throwable getException() {
        return throwable;
    }

    public Data getData() {
        return data;
    }
}
