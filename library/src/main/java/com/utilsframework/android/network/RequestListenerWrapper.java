package com.utilsframework.android.network;

public class RequestListenerWrapper<Result, ErrorType> extends RequestListener<Result, ErrorType> {
    private RequestListener<Result, ErrorType> requestListener;

    public RequestListenerWrapper(RequestListener<Result, ErrorType> requestListener) {
        this.requestListener = requestListener;
    }

    @Override
    public void onSuccess(Result result) {
        if (requestListener != null) {
            requestListener.onSuccess(result);
        }
    }

    @Override
    public void onError(ErrorType e) {
        if (requestListener != null) {
            requestListener.onError(e);
        }
    }

    @Override
    public void onCanceled() {
        if (requestListener != null) {
            requestListener.onCanceled();
        }
    }

    @Override
    public void onAfterCompleteOrCanceled() {
        if (requestListener != null) {
            requestListener.onAfterCompleteOrCanceled();
        }
    }

    @Override
    public void onSuccessOrError() {
        if (requestListener != null) {
            requestListener.onSuccessOrError();
        }
    }

    @Override
    public void onPreExecute() {
        if (requestListener != null) {
            requestListener.onPreExecute();
        }
    }
}
