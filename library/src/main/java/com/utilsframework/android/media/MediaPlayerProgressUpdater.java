package com.utilsframework.android.media;

import android.media.MediaPlayer;
import com.utils.framework.Destroyable;
import com.utilsframework.android.UiLoopEvent;

/**
 * User: Tikhonenko.S
 * Date: 21.08.14
 * Time: 21:06
 */
public abstract class MediaPlayerProgressUpdater implements Destroyable {
    private UiLoopEvent uiLoopEvent;
    private long lastDuration = -1;
    private long lastCurrentPosition = -1;

    public void setMediaPlayer(final MediaPlayer mediaPlayer) {
        if(mediaPlayer == null){
            throw new NullPointerException();
        }

        if(uiLoopEvent != null){
            uiLoopEvent.stop();
        }

        uiLoopEvent = new UiLoopEvent();
        uiLoopEvent.run(new Runnable() {
            @Override
            public void run() {
                boolean shouldCallUpdateListener = false;

                if(!mediaPlayer.isPlaying()){
                    return;
                }

                try {
                    long currentPosition = mediaPlayer.getCurrentPosition();
                    if(lastCurrentPosition != currentPosition){
                        lastCurrentPosition = currentPosition;
                        shouldCallUpdateListener = true;
                    }

                } catch (IllegalStateException e) {

                }

                try {
                    long currentDuration = mediaPlayer.getDuration();
                    if(lastDuration != currentDuration){
                        lastDuration = currentDuration;
                        shouldCallUpdateListener = true;
                    }

                } catch (IllegalStateException e) {

                }

                if(shouldCallUpdateListener){
                    onProgressChanged(lastCurrentPosition, lastDuration);
                }
            }
        });
    }

    protected abstract void onProgressChanged(long progress, long max);

    @Override
    public void destroy() {
        uiLoopEvent.stop();
    }
}
