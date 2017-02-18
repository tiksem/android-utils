package com.utilsframework.android.network.retrofit;

import com.utilsframework.android.network.BaseRequestManager;
import com.utilsframework.android.network.CancelStrategy;
import com.utilsframework.android.network.RequestListener;
import com.utilsframework.android.threading.AbstractCancelable;
import com.utilsframework.android.threading.Cancelable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitRequestManager<ErrorData> extends BaseRequestManager
        implements RetrofitRequestExecutor<ErrorData> {
    @Override
    public <Result> void executeCall(final Call<Result> call,
                                     final RequestListener<Result,
                                             RetrofitError<ErrorData>> requestListener,
                                     final CancelStrategy cancelStrategy) {
        if (requestListener != null) {
            requestListener.onPreExecute();
        }

        final Cancelable cancelable = new AbstractCancelable() {
            @Override
            protected void doCancel(boolean interrupt) {
                if (interrupt) {
                    call.cancel();
                }
            }
        };

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (requestListener != null) {
                    if (cancelable.isCancelled()) {
                        requestListener.onCanceled();
                    } else if(response.isSuccessful()) {
                        requestListener.onSuccess(response.body());
                    } else {
                        ErrorData errorData = createErrorData(response);
                        RetrofitError<ErrorData> retrofitError = new RetrofitError<ErrorData>(null,
                                errorData);
                        requestListener.onError(retrofitError);
                    }

                    requestListener.onAfterCompleteOrCanceled();
                }

                notifyTaskCompleted(cancelable);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                if (requestListener != null) {
                    if (call.isCanceled()) {
                        requestListener.onCanceled();
                        requestListener.onAfterCompleteOrCanceled();
                    } else {
                        requestListener.onError(new RetrofitError<ErrorData>(t, null));
                        requestListener.onSuccessOrError();
                    }

                    requestListener.onAfterCompleteOrCanceled();
                }

                notifyTaskCompleted(cancelable);
            }
        });

        notifyTaskExecuting(cancelable, cancelStrategy);
    }

    protected abstract <Data> ErrorData createErrorData(Response<Data> response);
}
