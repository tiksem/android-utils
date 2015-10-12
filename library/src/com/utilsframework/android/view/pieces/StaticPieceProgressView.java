package com.utilsframework.android.view.pieces;

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
public class StaticPieceProgressView extends LinearLayout{
    private List<Float> lastFragments;
    private PieceAdapter pieceAdapter;
    private PiecesProvider piecesProvider;
    private AsyncTask<Void, Void, Boolean> fragmentsUpdatingAsyncTask;
    private OnPieceClick onPieceClick;

    private OnFragmentsChanged mFragmentsListener;

    private Runnable fragmentsUpdatingTask = new Runnable() {
        List<Float> fragments;

        boolean fragmentsChanged(){
            fragments = pieceAdapter.getFragments();
            if(fragments == null){
                return false;
            }

            return !CollectionUtils.contentEquals(fragments, lastFragments);

        }

        @Override
        public void run() {
            if(pieceAdapter == null){
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
            if(onPieceClick != null){
                onPieceClick.onClick(index, value);
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

        if(pieceAdapter == null){
            throw new IllegalStateException("adapter is null");
        }

        int index = 0;
        for(float fragment : fragments){
            View view;

            if (piecesProvider == null) {
                view = pieceAdapter.getView(index, fragment, fragments.size());
            } else {
                view = getChildAt(index);
            }

            View.OnClickListener onClickListener = new ViewClickListener(index, fragment);
            view.setOnClickListener(onClickListener);

            if (piecesProvider == null) {
                addView(view);
            }

            index++;
        }
    }

    public StaticPieceProgressView(Context context) {
        super(context);
    }

    public StaticPieceProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StaticPieceProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PieceAdapter getAdapter() {
        return pieceAdapter;
    }

    public OnPieceClick getOnFragmentClickListener() {
        return onPieceClick;
    }

    public void setOnFragmentClickListener(OnPieceClick onPieceClick) {
        this.onPieceClick = onPieceClick;
    }

    public boolean isGetFragmentsRunning(){
        return fragmentsUpdatingAsyncTask != null;
    }

    public void setAdapter(PieceAdapter pieceAdapter) {
        if(isGetFragmentsRunning()){
            throw new IllegalStateException("do not call setAdapter while getFragments is running");
        }

        this.pieceAdapter = pieceAdapter;
        this.piecesProvider = null;
        updateFragments();
    }

    public PiecesProvider getPiecesProvider() {
        return piecesProvider;
    }

    public void setPiecesProvider(final PiecesProvider piecesProvider) {
        this.piecesProvider = piecesProvider;

        pieceAdapter = new PieceAdapter() {
            @Override
            public View getView(int index, double sizeInPercents, int size) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<Float> getFragments() {
                int childCount = getChildCount();
                List<Float> result = new ArrayList<Float>(childCount);

                for(int i = 0; i < childCount; i++){
                    View child = getChildAt(i);
                    float fragment = piecesProvider.getFragment(i, child);
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
