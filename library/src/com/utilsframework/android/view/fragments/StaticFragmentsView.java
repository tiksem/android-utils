package com.utilsframework.android.view.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.utils.framework.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 14:17
 */
public class StaticFragmentsView extends LinearLayout{
    private List<Float> lastFragments;
    private FragmentsAdapter fragmentsAdapter;
    private FragmentsProvider fragmentsProvider;
    private AsyncTask<Void, Void, Boolean> fragmentsUpdatingAsyncTask;
    private OnFragmentClick onFragmentClick;

    private OnFragmentsChanged mFragmentsListener;

    private Runnable fragmentsUpdatingTask = new Runnable() {
        List<Float> fragments;

        boolean fragmentsChanged(){
            fragments = fragmentsAdapter.getFragments();
            if(fragments == null){
                return false;
            }

            return !CollectionUtils.contentEquals(fragments, lastFragments);

        }

        @Override
        public void run() {
            if(fragmentsAdapter == null){
                return;
            }

            if(!shouldRunGetFragmentsAsync()){
                if(fragmentsChanged()){
                    rebuildFragments(fragments);

                    if(mFragmentsListener != null) {
                        mFragmentsListener.onChanged();
                    }
                }
            } else {
                fragmentsUpdatingAsyncTask = new AsyncTask<Void, Void, Boolean>(){
                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        return fragmentsChanged();
                    }

                    @Override
                    protected void onPostExecute(Boolean fragmentsChanged) {
                        if (fragmentsChanged) {
                            rebuildFragments(fragments);

                            if(mFragmentsListener != null) {
                                mFragmentsListener.onChanged();
                            }
                        }
                        fragmentsUpdatingAsyncTask = null;
                    }
                }.execute();
            }
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(lastFragments == null || lastFragments.size() == 0){
            return;
        }

        boolean isHorizontal = getOrientation() == HORIZONTAL;

        int parentWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = View.MeasureSpec.getSize(heightMeasureSpec);

        int parentOrientationSize = parentWidth;
        if(!isHorizontal){
            parentOrientationSize = parentHeight;
        }

        float sum = (float) CollectionUtils.sum(lastFragments);
        float k = 1.0f;
        boolean sumIsNil = false;
        if(sum != 0){
            k = parentOrientationSize / sum;
        } else {
            sumIsNil = true;
            k = parentOrientationSize / lastFragments.size();
        }

        int index = 0;
        for(float fragment : lastFragments){
            if (!sumIsNil) {
                fragment *= k;
            } else {
                fragment = k;
            }

            View view = getChildAt(index);

            int childWidth;
            int childHeight;

            if (isHorizontal) {
                childWidth = MeasureSpec.makeMeasureSpec(Math.round(fragment), MeasureSpec.EXACTLY);
                childHeight = MeasureSpec.makeMeasureSpec(parentHeight,
                        MeasureSpec.EXACTLY);
            } else {
                childHeight = MeasureSpec.makeMeasureSpec(Math.round(fragment), MeasureSpec.EXACTLY);
                childWidth = MeasureSpec.makeMeasureSpec(parentWidth,
                        MeasureSpec.EXACTLY);
            }

            view.measure(childWidth, childHeight);

            index++;
        }
    }

    private class ViewClickListener implements View.OnClickListener {
        float value;
        int index;

        private ViewClickListener(int index, float value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public void onClick(View v) {
            if(onFragmentClick != null){
                onFragmentClick.onClick(index, value);
            }
        }
    }

    private void rebuildFragments(List<Float> fragments) {
        lastFragments = fragments;
        removeAllViewsInLayout();
        if(fragments.isEmpty()){
            requestLayout();
            invalidate();
            return;
        }

        if(fragmentsAdapter == null){
            throw new IllegalStateException("adapter is null");
        }

        int index = 0;
        for(float fragment : fragments){
            View view;

            if (fragmentsProvider == null) {
                view = fragmentsAdapter.getView(index, fragment);
            } else {
                view = getChildAt(index);
            }

            View.OnClickListener onClickListener = new ViewClickListener(index, fragment);
            view.setOnClickListener(onClickListener);

            if (fragmentsProvider == null) {
                addView(view);
            }

            index++;
        }
    }

    public StaticFragmentsView(Context context) {
        super(context);
    }

    public StaticFragmentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StaticFragmentsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FragmentsAdapter getAdapter() {
        return fragmentsAdapter;
    }

    public OnFragmentClick getOnFragmentClickListener() {
        return onFragmentClick;
    }

    public void setOnFragmentClickListener(OnFragmentClick onFragmentClick) {
        this.onFragmentClick = onFragmentClick;
    }

    public boolean isGetFragmentsRunning(){
        return fragmentsUpdatingAsyncTask != null;
    }

    public void setAdapter(FragmentsAdapter fragmentsAdapter) {
        if(isGetFragmentsRunning()){
            throw new IllegalStateException("do not call setAdapter while getFragments is running");
        }

        this.fragmentsAdapter = fragmentsAdapter;
        this.fragmentsProvider = null;
        updateFragments();
    }

    public FragmentsProvider getFragmentsProvider() {
        return fragmentsProvider;
    }

    public void setFragmentsProvider(final FragmentsProvider fragmentsProvider) {
        this.fragmentsProvider = fragmentsProvider;

        fragmentsAdapter = new FragmentsAdapter() {
            @Override
            public View getView(int index, double sizeInPercents) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<Float> getFragments() {
                int childCount = getChildCount();
                List<Float> result = new ArrayList<Float>(childCount);

                for(int i = 0; i < childCount; i++){
                    View child = getChildAt(i);
                    float fragment = fragmentsProvider.getFragment(i, child);
                    result.add(fragment);
                }

                return result;
            }
        };

        updateFragments();
    }

    public void updateFragments(){
        fragmentsUpdatingTask.run();
    }

    public void setOnFragmentsChanged(OnFragmentsChanged listener) {
        this.mFragmentsListener = listener;
    }

    protected boolean shouldRunGetFragmentsAsync(){
        return false;
    }

    public interface OnFragmentsChanged {
        public void onChanged();
    }
}
