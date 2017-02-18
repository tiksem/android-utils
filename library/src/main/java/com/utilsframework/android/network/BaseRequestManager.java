package com.utilsframework.android.network;

import com.utilsframework.android.threading.Cancelable;

import java.util.HashMap;
import java.util.Map;

public class BaseRequestManager implements RequestManager {
    private Map<Cancelable, CancelStrategy> runningRequests = new HashMap<>();

    @Override
    public void notifyTaskExecuting(Cancelable task, CancelStrategy cancelStrategy) {
        if (cancelStrategy != CancelStrategy.NO_CANCEL) {
            runningRequests.put(task, cancelStrategy);
        }
    }

    @Override
    public void notifyTaskCompleted(Cancelable task) {
        runningRequests.remove(task);
    }

    @Override
    public void cancelAll() {
        for (Map.Entry<Cancelable, CancelStrategy> entry : runningRequests.entrySet()) {
            Cancelable task = entry.getKey();
            CancelStrategy cancelStrategy = entry.getValue();
            task.cancel(cancelStrategy == CancelStrategy.INTERRUPT);
        }
    }
}
