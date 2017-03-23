package com.utilsframework.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utilsframework.android.network.CancelStrategy;
import com.utilsframework.android.network.LoadingContentVisibilityManager;
import com.utilsframework.android.network.RequestManager;
import com.utilsframework.android.network.retrofit.CallProvider;
import com.utilsframework.android.network.retrofit.RetrofitRequestManager;

import java.util.List;

public abstract class PageLoadingFragment extends RequestManagerFragment {
    private LoadingContentVisibilityManager<List, Throwable> contentVisibilityManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getRootLayoutId(), null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load();
    }

    private void load() {
        contentVisibilityManager = new LoadingContentVisibilityManager<List, Throwable>(getActivity()) {
            @Override
            public int getContentLayoutId() {
                return PageLoadingFragment.this.getContentLayoutId();
            }

            @Override
            public int getLoadingLayoutId() {
                return PageLoadingFragment.this.getLoadingLayoutId();
            }

            @Override
            public int getRetryButtonId() {
                return PageLoadingFragment.this.getRetryButtonId();
            }

            @Override
            public void onRetry() {
                load();
            }

            @Override
            public int getNoConnectionView() {
                return PageLoadingFragment.this.getNoConnectionView();
            }
        };
        getRequestManager().executeMultipleCalls(getCallProviders(),
                contentVisibilityManager, CancelStrategy.INTERRUPT);
    }

    public abstract int getContentLayoutId();
    public abstract int getLoadingLayoutId();
    public abstract int getRetryButtonId();
    public abstract int getNoConnectionView();

    public abstract List<CallProvider> getCallProviders();

    public View getPageContentView() {
        return contentVisibilityManager.getContentView();
    }

    public abstract int getRootLayoutId();

    @Override
    public RetrofitRequestManager getRequestManager() {
        return (RetrofitRequestManager) super.getRequestManager();
    }
}
