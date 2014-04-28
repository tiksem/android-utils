package com.utilsframework.android.threading;

/**
 * Created by Tikhonenko.S on 01.10.13.
 */
public class EmptyAsyncRequestTask<SuccessResult, ErrorResult> implements AsyncRequestTask<SuccessResult, ErrorResult>{
    @Override
    public void execute(OnTaskCompleted<SuccessResult, ErrorResult> onRequestCompleted) {
        onRequestCompleted.onSuccess(null);
    }
}
