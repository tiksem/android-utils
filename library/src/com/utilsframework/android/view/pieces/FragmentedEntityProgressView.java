package com.utilsframework.android.view.pieces;

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
    private PieceAdapter pieceAdapter;
    private PieceProgressView fragmentsView;
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

    public PieceAdapter getPieceAdapter() {
        return pieceAdapter;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int fragmentsViewId = getFragmentsViewId();
        fragmentsView = (PieceProgressView)findViewById(fragmentsViewId);
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

    private class PieceAdapterWrapper implements PieceAdapter {
        @Override
        public View getView(int index, double sizeInPercents, int size) {
            return pieceAdapter.getView(index, sizeInPercents, size);
        }

        @Override
        public List<Float> getFragments() {
            final List<Float> result = pieceAdapter.getFragments();
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

    public void setPieceAdapter(final PieceAdapter pieceAdapter) {
        this.pieceAdapter = pieceAdapter;
        showFragmentsView();
        if(pieceAdapter != null){
            fragmentsView.setAdapter(new PieceAdapterWrapper());
        } else {
            fragmentsView.setAdapter(null);
        }
    }

    public OnPieceClick getOnFragmentClickListener() {
        return fragmentsView.getOnFragmentClickListener();
    }

    public void setOnFragmentClickListener(OnPieceClick onPieceClick) {
        fragmentsView.setOnFragmentClickListener(onPieceClick);
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
