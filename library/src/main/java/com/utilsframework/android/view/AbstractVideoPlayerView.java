package com.utilsframework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ViewGroup containerLayout;
    private int currentPosition = 0;

    public AbstractVideoPlayerView(Context context) {
        super(context);
        construct();
    }

    public AbstractVideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        construct();
    }

    public AbstractVideoPlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        construct();
    }

    private void initViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        containerLayout = (ViewGroup) layoutInflater.inflate(getLayoutId(), this);
    }

    private void connectVideoViewAndMediaController(){
        mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
    }

    private void construct() {
        initViews();
    }

    public VideoView onStart() {
        videoView = new VideoView(getContext());
        addView(videoView);
        connectVideoViewAndMediaController();
        return videoView;
    }

    public void onStop() {
        videoView.setOnCompletionListener(null);
        currentPosition = videoView.getCurrentPosition();
        videoView.stopPlayback();
        containerLayout.removeView(videoView);

    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public VideoView getVideoView() {
        return videoView;
    }

    public MediaController getMediaController() {
        return mediaController;
    }

    protected abstract int getLayoutId();
}
