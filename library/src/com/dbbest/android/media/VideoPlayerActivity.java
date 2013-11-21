package com.dbbest.android.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import com.dbbest.android.R;
import com.dbbest.android.view.VideoPlayerView;

import java.io.File;
import java.net.URI;

/**
 * User: Tikhonenko.S
 * Date: 21.11.13
 * Time: 16:28
 */
public class VideoPlayerActivity extends Activity{
    public static final String URI = "uri";

    public static void start(Context from, String uri){
        Intent intent = new Intent(from, VideoPlayerActivity.class);
        intent.putExtra(URI, uri);
        from.startActivity(intent);
    }

    public static void start(Context from, File uri){
        start(from, uri.getAbsolutePath());
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
        videoView.start();
        setContentView(videoPlayerView);
    }
}
