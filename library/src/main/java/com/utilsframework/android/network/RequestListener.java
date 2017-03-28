package com.utilsframework.android.network;

public abstract class RequestListener<Result, ErrorType> {
    public void onSuccess(Result result) {}
    public void onError(ErrorType e) {}
    public void onCanceled() {}
    public void onAfterCompleteOrCanceled() {}
    public void onSuccessOrError() {}
    public void onPreExecute() {}
}