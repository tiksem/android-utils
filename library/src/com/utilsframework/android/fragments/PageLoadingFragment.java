package com.utilsframework.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.utilsframework.android.R;
import com.utilsframework.android.threading.Threading;

import java.io.IOException;

/**
 * Created by CM on 7/2/2015.
 */
public abstract class PageLoadingFragment<Data, ErrorType extends Throwable> extends Fragment {
    private View content;
    private View loading;
    private View noConnection;
    private Class<ErrorType> errorTypeClass;
    private Data data;

    public PageLoadingFragment(Class<ErrorType> errorTypeClass) {
        this.errorTypeClass = errorTypeClass;
    }

    protected abstract int getContentLayoutId();

    protected int getLoadingLayoutId() {
        return R.layout.page_loading;
    }

    protected int getNoConnectionLayoutId() {
        return R.layout.no_internet_connection;
    }

    protected int getRetryLoadingButtonId() {
        return 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout result = new FrameLayout(getActivity());
        content = inflater.inflate(getContentLayoutId(), null);
        loading = inflater.inflate(getLoadingLayoutId(), null);
        noConnection = inflater.inflate(getNoConnectionLayoutId(), null);

        result.addView(content);
        result.addView(loading);
        result.addView(noConnection);
        return result;
    }

    protected abstract Data loadOnBackground() throws ErrorType;

    private void showLoading() {
        content.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
        noConnection.setVisibility(View.INVISIBLE);
    }

    private void onDataLoaded(Data data) {
        this.data = data;
        loading.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
        noConnection.setVisibility(View.INVISIBLE);
        setupContent(data, content);
    }

    protected void onError(ErrorType error) {
        loading.setVisibility(View.INVISIBLE);
        noConnection.setVisibility(View.VISIBLE);
    }

    protected abstract void setupContent(Data data, View content);

    private void reloadPage() {
        showLoading();
        Threading.executeAsyncTask(new PageLoadingTask<>(this), errorTypeClass);
    }

    void onDataLoadingFinished(Data data, ErrorType error) {
        if (error != null) {
            PageLoadingFragment.this.onError(error);
        } else {
            onDataLoaded(data);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int retryButtonId = getRetryLoadingButtonId();
        if (retryButtonId != 0) {
            noConnection.findViewById(retryButtonId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reloadPage();
                }
            });
        }

        if (data == null) {
            reloadPage();
        } else {
            onDataLoaded(data);
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public View getContent() {
        return content;
    }
}
