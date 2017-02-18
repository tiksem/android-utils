package com.utilsframework.android.network;

import com.utilsframework.android.threading.Cancelable;

public interface RequestManager {
    void notifyTaskCompleted(Cancelable task);
    void notifyTaskExecuting(Cancelable task, CancelStrategy cancelStrategy);
    void cancelAll();
}
