package com.utilsframework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * User: Tikhonenko.S
 * Date: 21.11.13
 * Time: 17:20
 */
public abstract class AbstractVideoPlayerView extends FrameLayout{
    private VideoView videoView;
    private MediaController mediaController;

    public AbstractVideoPlayerView(Context context) {
        super(context);
        init();
    }

    public AbstractVideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AbstractVideoPlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void initViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View root = layoutInflater.inflate(getLayoutId(), this);

        int videoViewId = getVideoViewId();
        videoView = (VideoView)root.findViewById(videoViewId);
        if(videoView == null){
            throw new RuntimeException("Could not find VideoView with id = " + videoViewId);
        }
    }

    private void connectVideoViewAndMediaController(){
        mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
    }

    private void init() {
        initViews();
        connectVideoViewAndMediaController();
    }

    public VideoView getVideoView() {
        return videoView;
    }

    public MediaController getMediaController() {
        return mediaController;
    }

    protected abstract int getVideoViewId();
    protected abstract int getLayoutId();
}
