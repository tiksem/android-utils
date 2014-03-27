package com.dbbest.android.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;
import com.dbbest.android.media.MediaPlayerProvider;

/**
 * User: Tikhonenko.S
 * Date: 26.03.14
 * Time: 18:52
 */
public class ExtendedVideoView extends VideoView implements MediaPlayerProvider{
    private MediaPlayer.OnPreparedListener onPreparedListener;
    private MediaPlayer mediaPlayer;

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
}
