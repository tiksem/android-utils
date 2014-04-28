package com.utilsframework.android.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import com.utilsframework.android.UiLoopEvent;
import com.utilsframework.android.media.MediaPlayerProvider;

/**
 * User: Tikhonenko.S
 * Date: 26.03.14
 * Time: 18:49
 */
public class SoundMuteButton extends ToggleButton{
    private MediaPlayerProvider mediaPlayerProvider;
    private UiLoopEvent mediaPlayerUpdater;
    private static final int MEDIA_PLAYER_UPDATE_DELAY = 100;

    public SoundMuteButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SoundMuteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SoundMuteButton(Context context) {
        super(context);
        init();
    }

    public MediaPlayerProvider getMediaPlayerProvider() {
        return mediaPlayerProvider;
    }

    public void setMediaPlayerProvider(MediaPlayerProvider mediaPlayerProvider) {
        this.mediaPlayerProvider = mediaPlayerProvider;
    }

    private void updateCheckedState(){
        if(mediaPlayerProvider == null){
            return;
        }

        MediaPlayer mediaPlayer = mediaPlayerProvider.getMediaPlayer();
        if(mediaPlayer != null){
            boolean mute = isChecked();
            float volume = mute ? 0.0f : 1.0f;
            try {
                mediaPlayer.setVolume(volume, volume);
            } catch (IllegalStateException e) {

            }
        }
    }

    @Override
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    private void init(){
        super.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateCheckedState();
            }
        });

        mediaPlayerUpdater = new UiLoopEvent(getContext(), MEDIA_PLAYER_UPDATE_DELAY);
        mediaPlayerUpdater.run(new Runnable() {
            @Override
            public void run() {
                updateCheckedState();
            }
        });
    }
}
