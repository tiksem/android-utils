package com.utilsframework.android.network.retrofit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.utilsframework.android.network.BaseRequestManager;
import com.utilsframework.android.network.CancelStrategy;
import com.utilsframework.android.network.HttpResponseException;
import com.utilsframework.android.network.RequestListener;
import com.utilsframework.android.threading.AbstractCancelable;
import com.utilsframework.android.threading.Cancelable;
import com.utilsframework.android.threading.MainThreadExecutor;
import com.utilsframework.android.threading.ResultTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitRequestManager extends BaseRequestManager
        implements RetrofitRequestExecutor {
    private static final String TAG = RetrofitRequestManager.class.getSimpleName();

    private Set<RetrofitRequestManagerResponseErrorInterceptor> errorInterceptors;
    @Override
    public <Result> void executeCall(final Call<Result> call,
                                     final RequestListener<Result, Throwable> requestListener,
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
                        requestListener.onSuccess(response);
                        requestListener.onSuccessOrError();
                    } else {
                        onResponseError(response, requestListener);
                        requestListener.onSuccessOrError();
                    }

                    requestListener.onAfterCompleteOrCanceled();
                }

                notifyTaskCompleted(cancelable);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                if (t != null) {
                    Log.i(TAG, "request failed", t);
                }

                if (requestListener != null) {
                    if (call.isCanceled()) {
                        requestListener.onCanceled();
                        requestListener.onAfterCompleteOrCanceled();
                    } else {
                        requestListener.onError(t);
                        requestListener.onSuccessOrError();
                    }

                    requestListener.onAfterCompleteOrCanceled();
                }

                notifyTaskCompleted(cancelable);
            }
        });

        notifyTaskExecuting(cancelable, cancelStrategy);
    }

    private <Result> void onResponseError(Response<Result> response,
                                          RequestListener<Result, Throwable> requestListener) {
        if (errorInterceptors != null) {
            boolean errorHandled = false;
            for (RetrofitRequestManagerResponseErrorInterceptor interceptor
                    : errorInterceptors) {
                if (interceptor.onResponseError(response)) {
                    errorHandled = true;
                    break;
                }
            }

            if (!errorHandled) {
                executeResponseUserErrorListener(response, requestListener);
            }
        } else {
            executeResponseUserErrorListener(response, requestListener);
        }
    }

    private <Result> void executeResponseUserErrorListener(Response<Result> response,
                                                           RequestListener<Result, Throwable> requestListener) {
        try {
            HttpResponseException e = getHttpResponseException(response);
            requestListener.onError(e);
        } catch (IOException e) {
            e.printStackTrace();
            requestListener.onError(e);
        }
    }

    @NonNull
    private <Result> HttpResponseException getHttpResponseException(Response<Result> response) throws IOException {
        Object data = getHttpResponseExceptionData(response);
        String message = getErrorMessage(data, response);
        HttpResponseException e = new HttpResponseException(response, message);
        e.setData(data);
        return e;
    }

    @Override
    public void executeMultipleCalls(final List<CallProvider> callProviders,
                                     final RequestListener<List, Throwable> requestListener,
                                     CancelStrategy cancelStrategy) {
        new ResultTask<List, Throwable>(this, requestListener) {
            @Override
            protected List getResultInBackground() throws Throwable {
                List result = new ArrayList();
                for (final CallProvider callProvider : callProviders) {
                    final Response response = callProvider.getCall().execute();
                    if (response.isSuccessful()) {
                        final Object body = response.body();
                        result.add(body);
                        MainThreadExecutor.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callProvider.onSuccess(response);
                                callProvider.onSuccess(body);
                            }
                        });
                    } else {
                        throw getHttpResponseException(response);
                    }
                }

                return result;
            }

            @Override
            protected void onError(Throwable e) {
                if (e instanceof HttpResponseException) {
                    HttpResponseException httpResponseException = (HttpResponseException)e;
                    onResponseError(httpResponseException.getResponse(), requestListener);
                } else {
                    super.onError(e);
                }
            }
        }.execute();
    }

    public void setErrorInterceptors(Set<RetrofitRequestManagerResponseErrorInterceptor>
                                             errorInterceptors) {
        this.errorInterceptors = errorInterceptors;
    }

    protected <T> Object getHttpResponseExceptionData(Response<T> response)
            throws IOException {
        return null;
    }

    protected String getErrorMessage(Object errorData, Response response) {
        if (errorData != null) {
            return errorData.toString();
        } else {
            return "Http response error " + response.code();
        }
    }
}
