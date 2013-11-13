package com.dbbest.android.threading;

/**
 * Created by Tikhonenko.S on 27.09.13.
 */
public interface OnTaskCompleted<SuccessResult,ErrorResult> {
    void onError(ErrorResult errorResult);
    void onSuccess(SuccessResult successResult);
}
