package com.utilsframework.android.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;
import com.utilsframework.android.Pauseable;
import com.utilsframework.android.media.MediaPlayerProvider;

/**
 *
 * User: Tikhonenko.S
 * Date: 26.03.14
 * Time: 18:52
 */
public class ExtendedVideoView extends VideoView implements MediaPlayerProvider, Pauseable{
    private MediaPlayer.OnPreparedListener onPreparedListener;
    private MediaPlayer mediaPlayer;
    private boolean paused = false;

    public ExtendedVideoView(Context context) {
        super(context);
        init();
    }

    public ExtendedVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExtendedVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        super.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;

                if(onPreparedListener != null){
                    onPreparedListener.onPrepared(mp);
                }
            }
        });
    }

    @Override
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        onPreparedListener = listener;
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void pause() {
        super.pause();
        paused = true;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void resume() {
        super.start();
        paused = false;
    }
}
