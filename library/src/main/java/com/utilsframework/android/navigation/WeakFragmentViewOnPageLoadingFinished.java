package com.utilsframework.android.navigation;

import android.support.v4.app.Fragment;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.WeakFragmentReferenceHolder;

import java.util.List;

/**
 * Created by CM on 9/18/2015.
 */
public abstract class WeakFragmentViewOnPageLoadingFinished<FragmentType extends Fragment, Element>
        extends WeakFragmentReferenceHolder<FragmentType> implements NavigationList.OnPageLoadingFinished<Element> {
    public WeakFragmentViewOnPageLoadingFinished(FragmentType fragment) {
        super(fragment);
    }

    @Override
    public final void onLoadingFinished(List<Element> elements) {
        if (getView() != null) {
            onLoadingFinished(elements, getFragment());
        }
    }

    protected abstract void onLoadingFinished(List<Element> elements, FragmentType fragment);
}
