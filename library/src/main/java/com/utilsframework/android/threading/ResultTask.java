package com.utilsframework.android.threading;

import android.os.AsyncTask;

import com.utilsframework.android.network.CancelStrategy;
import com.utilsframework.android.network.RequestListener;
import com.utilsframework.android.network.RequestManager;

public abstract class ResultTask<Result, ErrorType extends Throwable>
        extends AsyncTask<Void, Void, Result> implements Cancelable {
    private ErrorType error;
    private RequestListener<Result, ErrorType> requestListener;
    private RequestManager requestManager;
    private CancelStrategy cancelStrategy;

    public ResultTask(RequestManager requestManager,
                      RequestListener<Result, ErrorType> requestListener) {
        this(requestManager, requestListener, CancelStrategy.INTERRUPT);
    }

    public ResultTask(RequestManager requestManager,
                      RequestListener<Result, ErrorType> requestListener,
                      CancelStrategy cancelStrategy) {
        this.requestListener = requestListener;
        this.requestManager = requestManager;
        this.cancelStrategy = cancelStrategy;
    }



    @Override
    protected void onPreExecute() {
        if (requestManager != null) {
            requestManager.notifyTaskExecuting(this, cancelStrategy);
        }
        if (requestListener != null) {
            requestListener.onPreExecute();
        }
    }

    @Override
    protected final Result doInBackground(Void... params) {
        try {
            return getResultInBackground();
        } catch (Throwable error) {
            if (error instanceof Error) {
                throw new ExecutionException(error);
            } else if(error instanceof RuntimeException) {
                throw new ExecutionException(error);
            } else {
                this.error = (ErrorType)error;
            }

            return null;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (error != null) {
            onError(error);
        } else {
            onSuccess(result);
        }

        if (requestListener != null) {
            if (error != null) {
                requestListener.onError(error);
            } else {
                requestListener.onSuccess(result);
            }

            requestListener.onSuccessOrError();
            onAfterCompleteOrCanceled();
            requestListener.onAfterCompleteOrCanceled();
        }

        if (requestManager != null) {
            requestManager.notifyTaskCompleted(this);
        }
    }

    protected void onSuccess(Result result) {}
    protected void onError(ErrorType e) {}
    protected void onAfterCompleteOrCanceled() {}

    @Override
    protected void onCancelled(Result result) {
        requestManager.notifyTaskCompleted(this);
        if (requestListener != null) {
            requestListener.onCanceled();
            requestListener.onAfterCompleteOrCanceled();
        }
    }

    protected abstract Result getResultInBackground() throws ErrorType;
}
