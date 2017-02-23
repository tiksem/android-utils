package com.utilsframework.android.network;

import android.app.Activity;
import android.view.View;

import com.utilsframework.android.view.OneVisibleViewInGroupToggle;

public abstract class LoadingContentVisibilityManager<T, ErrorType> extends RequestListener<
        T, ErrorType> {
    private final View contentView;
    private final View loadingView;
    private final View noConnectionView;
    private final OneVisibleViewInGroupToggle visibilityToggle;

    public LoadingContentVisibilityManager(Activity activity) {
        contentView = activity.findViewById(getContentLayoutId());
        loadingView = activity.findViewById(getLoadingLayoutId());
        noConnectionView = activity.findViewById(getNoConnectionView());
        visibilityToggle = new OneVisibleViewInGroupToggle(loadingView, contentView,
                noConnectionView);
        int retryButtonId = getRetryButtonId();
        if (retryButtonId != 0) {
            activity.findViewById(getRetryButtonId()).setOnClickListener(
                    new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRetry();
                }
            });
        }
    }

    @Override
    public void onPreExecute() {
        visibilityToggle.makeVisible(loadingView);
    }

    @Override
    public void onSuccess(T t) {
        visibilityToggle.makeVisible(contentView);
    }

    @Override
    public void onError(ErrorType e) {
        visibilityToggle.makeVisible(noConnectionView);
    }

    public abstract int getContentLayoutId();
    public abstract int getLoadingLayoutId();
    public abstract int getNoConnectionView();
    public int getRetryButtonId() {
        return 0;
    }
    public void onRetry() {

    }
}
