package com.utilsframework.android.network;

import com.utilsframework.android.network.retrofit.CallProvider;
import com.utilsframework.android.network.retrofit.RetrofitRequestManager;

import java.util.List;

import retrofit2.Call;

public class RetrofitRequestExecutionManager implements AsyncRequestExecutionManager {
    private RetrofitRequestManager requestManager;

    public RetrofitRequestExecutionManager(RetrofitRequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public void execute(Object requestObject, RequestListener requestListener,
                        CancelStrategy cancelStrategy) {
        requestManager.executeCall((Call) requestObject, requestListener, cancelStrategy);
    }

    @Override
    public void executeMultipleRequests(Object requestsObject,
                                        RequestListener requestListener,
                                        CancelStrategy cancelStrategy) {
        requestManager.executeMultipleCalls((List<CallProvider>) requestsObject,
                requestListener, cancelStrategy);
    }
}
