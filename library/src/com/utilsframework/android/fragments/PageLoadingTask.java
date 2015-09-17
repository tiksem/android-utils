package com.utilsframework.android.fragments;

import android.app.Fragment;
import com.utilsframework.android.threading.Threading;

/**
 * Created by CM on 9/18/2015.
 */
class PageLoadingTask<Data, ErrorType extends Throwable> extends
        WeakFragmentReferenceHolder<PageLoadingFragment<Data, ErrorType>> implements Threading.Task<ErrorType, Data> {
    public PageLoadingTask(PageLoadingFragment<Data, ErrorType> fragment) {
        super(fragment);
    }

    @Override
    public Data runOnBackground() throws ErrorType {
        return getFragment().loadOnBackground();
    }

    @Override
    public void onComplete(Data data, ErrorType error) {
        if (getView() != null) {
            getFragment().onDataLoadingFinished(data, error);
        }
    }
}
