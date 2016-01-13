package com.utilsframework.android.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;
import com.utilsframework.android.view.VideoPlayerView;

import java.io.File;

/**
 * User: Tikhonenko.S
 * Date: 21.11.13
 * Time: 16:28
 */
public class VideoPlayerActivity extends Activity{
    public static final String URI = "uri";
    public static final String SHOULD_FINISH_ACTIVITY = "SHOULD_FINISH_ACTIVITY";
    private VideoPlayerView videoPlayerView;
    private VideoView videoView;
    private String uri;
    private boolean shouldFinishActivityAfterPlay;

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
        uri = intent.getStringExtra(URI);
        if(uri == null){
            throw new IllegalArgumentException("VideoPlayerActivity starting intent should have " + URI + " param");
        }

        shouldFinishActivityAfterPlay = intent.getBooleanExtra(SHOULD_FINISH_ACTIVITY, false);

        videoPlayerView = new VideoPlayerView(this);
        setContentView(videoPlayerView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        videoView = videoPlayerView.onStart();
        videoView.setVideoPath(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.getDuration();
            }
        });

        if(shouldFinishActivityAfterPlay){
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    finish();
                }
            });
        }

        videoView.seekTo(videoPlayerView.getCurrentPosition());
        videoView.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoPlayerView.onStop();
    }
}
