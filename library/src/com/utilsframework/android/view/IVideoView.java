package com.utilsframework.android.view;

import android.media.MediaPlayer;
import android.widget.VideoView;
import com.utilsframework.android.Pauseable;
import com.utilsframework.android.media.MediaPlayerProvider;

/**
 * User: Tikhonenko.S
 * Date: 05.05.14
 * Time: 19:58
 */
public interface IVideoView extends Pauseable, MediaPlayerProvider{
    void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompleteListener);
    void start();
}
