package com.dbbest.android.threading;

/**
 * Created by Tikhonenko.S on 27.09.13.
 */
public interface AsyncRequestTask<SuccessResult,ErrorResult> {
    void execute(OnTaskCompleted<SuccessResult, ErrorResult> onRequestCompleted);
}
