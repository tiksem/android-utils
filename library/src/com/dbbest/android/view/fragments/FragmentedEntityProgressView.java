package com.dbbest.android.view.fragments;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 20.11.13
 * Time: 14:00
 */
public abstract class FragmentedEntityProgressView extends FrameLayout{
    private FragmentsAdapter fragmentsAdapter;
    private FragmentsView fragmentsView;
    private ProgressBar progressBar;
    private boolean alwaysShowFragments = false;

    public FragmentedEntityProgressView(Context context) {
        super(context);
    }

    public FragmentedEntityProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentedEntityProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FragmentsAdapter getFragmentsAdapter() {
        return fragmentsAdapter;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int fragmentsViewId = getFragmentsViewId();
        fragmentsView = (FragmentsView)findViewById(fragmentsViewId);
        if(fragmentsView == null){
            throw new RuntimeException("Could not find FragmentsView with id = " + fragmentsViewId);
        }

        int progressBarId = getProgressBarId();
        progressBar = (ProgressBar)findViewById(progressBarId);
        if(progressBar == null){
            throw new RuntimeException("Could not find ProgressBar with id = " + progressBarId);
        }

        showFragmentsView();
    }

    public boolean isAlwaysShowFragments() {
        return alwaysShowFragments;
    }

    public void setAlwaysShowFragments(boolean alwaysShowFragments) {
        this.alwaysShowFragments = alwaysShowFragments;
        if(alwaysShowFragments){
            showFragmentsView();
        }
    }

    private void showProgressBar(){
        if (!alwaysShowFragments) {
            progressBar.setVisibility(VISIBLE);
            fragmentsView.setVisibility(GONE);
        } else {
            showFragmentsView();
        }
    }

    private void showProgressBar(float progress){
        showProgressBar();
        progressBar.setProgress(Math.round(progress));
    }

    private void showFragmentsView(){
        progressBar.setVisibility(GONE);
        fragmentsView.setVisibility(VISIBLE);
    }

    protected void executeUpdate(Runnable runnable){
        post(runnable);
    }

    private class FragmentsAdapterWrapper implements FragmentsAdapter{
        @Override
        public View getView(int index, double sizeInPercents) {
            return fragmentsAdapter.getView(index, sizeInPercents);
        }

        @Override
        public List<Float> getFragments() {
            final List<Float> result = fragmentsAdapter.getFragments();
            int size = result.size();
            if(size == 1){
                executeUpdate(new Runnable() {
                    @Override
                    public void run() {
                        float progress = result.get(result.size() - 1);
                        showProgressBar(progress);
                    }
                });
            } else if(size != 0) {
                executeUpdate(new Runnable() {
                    @Override
                    public void run() {
                        showFragmentsView();
                    }
                });
            } else {
                fragmentsView.setVisibility(GONE);
                progressBar.setVisibility(VISIBLE);
            }

            return result;
        }
    }

    public void setFragmentsAdapter(final FragmentsAdapter fragmentsAdapter) {
        this.fragmentsAdapter = fragmentsAdapter;
        showFragmentsView();
        if(fragmentsAdapter != null){
            fragmentsView.setAdapter(new FragmentsAdapterWrapper());
        } else {
            fragmentsView.setAdapter(null);
        }
    }

    public OnFragmentClick getOnFragmentClickListener() {
        return fragmentsView.getOnFragmentClickListener();
    }

    public void setOnFragmentClickListener(OnFragmentClick onFragmentClick) {
        fragmentsView.setOnFragmentClickListener(onFragmentClick);
    }

    public void pauseViewUpdating(){
        fragmentsView.pauseFragmentsUpdating();
    }

    public void resumeViewUpdating(){
        fragmentsView.resumeFragmentsUpdating();
    }

    public void updateFragments(){
        fragmentsView.updateFragments();
    }

    protected abstract int getProgressBarId();
    protected abstract int getFragmentsViewId();
}
