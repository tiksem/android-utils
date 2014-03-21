package com.dbbest.android.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;
import com.dbbest.android.view.VideoPlayerView;

import java.io.File;

/**
 * User: Tikhonenko.S
 * Date: 21.11.13
 * Time: 16:28
 */
public class VideoPlayerActivity extends Activity{
    public static final String URI = "uri";
    public static final String SHOULD_FINISH_ACTIVITY = "SHOULD_FINISH_ACTIVITY";

    public static void start(Context from, String uri, boolean shouldFinishActivityAfterPlay){
        Intent intent = new Intent(from, VideoPlayerActivity.class);
        intent.putExtra(URI, uri);
        if (shouldFinishActivityAfterPlay) {
            intent.putExtra(SHOULD_FINISH_ACTIVITY, shouldFinishActivityAfterPlay);
        }
        from.startActivity(intent);
    }

    public static void start(Context from, File uri){
        start(from, uri.getAbsolutePath(), false);
    }

    public static void start(Context from, String uri){
        start(from, uri, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String uri = intent.getStringExtra(URI);
        if(uri == null){
            throw new IllegalArgumentException("VideoPlayerActivity starting intent should have " + URI + " param");
        }

        VideoPlayerView videoPlayerView = new VideoPlayerView(this);
        final VideoView videoView = videoPlayerView.getVideoView();
        videoView.setVideoPath(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.getDuration();
            }
        });

        if(intent.getBooleanExtra(SHOULD_FINISH_ACTIVITY, false)){
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    finish();
                }
            });
        }

        videoView.start();
        setContentView(videoPlayerView);
    }
}
