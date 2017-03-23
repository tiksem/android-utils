package com.utilsframework.android.network;

public interface AsyncRequestExecutionManager {
    void execute(Object requestObject, RequestListener requestListener,
                 CancelStrategy cancelStrategy);
    void executeMultipleRequests(Object requestsObject,
                                 RequestListener requestListener,
                                 CancelStrategy cancelStrategy);
}
