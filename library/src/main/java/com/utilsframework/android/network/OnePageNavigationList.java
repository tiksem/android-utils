package com.utilsframework.android.network;

import com.utils.framework.OnError;
import com.utils.framework.collections.NavigationList;
import com.utils.framework.collections.OnLoadingFinished;
import com.utilsframework.android.threading.Threading;

import java.io.IOException;
import java.util.List;

/**
 * Created by stykhonenko on 23.10.15.
 */
public abstract class OnePageNavigationList<T> extends NavigationList<T> {
    private RequestManager requestManager;

    public OnePageNavigationList(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public void getElementsOfPage(int pageNumber, final OnLoadingFinished<T> onPageLoadingFinished, final OnError onError) {
        requestManager.execute(new Threading.Task<IOException, List<T>>() {
            @Override
            public List<T> runOnBackground() throws IOException {
                return load();
            }

            @Override
            public void onComplete(List<T> page, IOException error) {
                if (error == null) {
                    onPageLoadingFinished.onLoadingFinished(page, true);
                } else {
                    if (onError != null) {
                        onError.onError(error);
                    }
                }
            }
        });
    }

    protected abstract List<T> load() throws IOException;
}
