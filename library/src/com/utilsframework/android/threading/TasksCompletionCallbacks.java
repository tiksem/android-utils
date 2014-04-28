package com.utilsframework.android.threading;

import java.util.List;

/**
 * Created by Tikhonenko.S on 27.09.13.
 */
public interface TasksCompletionCallbacks<SuccessResult, ErrorResult> {
    public interface Result<SuccessResult, ErrorResult>{
        ErrorResult getError();
        SuccessResult getSuccessResult();
        AsyncRequestTask<SuccessResult, ErrorResult> getTask();
    }

    void onAllComplete(List<Result<SuccessResult, ErrorResult>> results);
    void onComplete(Result<SuccessResult, ErrorResult> result);
}
