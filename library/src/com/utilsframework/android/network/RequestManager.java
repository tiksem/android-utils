package com.utilsframework.android.network;

import com.utilsframework.android.threading.OnFinish;
import com.utilsframework.android.threading.Threading;
import com.utilsframework.android.threading.ThrowingRunnable;

import java.io.IOException;

/**
 * Created by stykhonenko on 12.10.15.
 * Use implementation of RequestManager (for example: AsyncRequestExecutorManager) in your activity/fragment/view
 * lifecycle. Create requestManager on onCreate/onAttach/onAttachedToWindow. Call cancelAll on onDestroy/onDetach/
 * onDetachedFromWindow etc. This is used to avoid fragment/activity memory leaks
 * and cancel unused network requests executions.
 */
public interface RequestManager {
    <Result> void execute(Threading.Task<IOException, Result> task);
    void execute(ThrowingRunnable<IOException> runnable, OnFinish<IOException> onFinish);
    void cancelAll();
}
