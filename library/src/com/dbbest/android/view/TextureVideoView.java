package com.dbbest.android.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import com.dbbest.android.IOErrorListener;
import com.dbbest.android.Pauseable;
import com.dbbest.android.media.MediaPlayerProvider;
import com.dbbest.android.threading.Tasks;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * User: Tikhonenko.S
 * Date: 02.04.14
 * Time: 18:32
 */
public class TextureVideoView extends TextureView implements Pauseable, MediaPlayerProvider{
    private MediaPlayer mediaPlayer;
    private boolean paused = false;
    private Deque<Runnable> onSurfaceTextureAvailableTasks = new ArrayDeque<Runnable>();
    private String videoPath;
    private IOErrorListener ioErrorListener;

    private SurfaceTextureListener surfaceTextureListener = new SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            mediaPlayer = new MediaPlayer();

            Tasks.executeAndClearQueue(onSurfaceTextureAvailableTasks);
            onSurfaceTextureAvailableTasks = null;

            mediaPlayer.setSurface(new Surface(surface));
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    public TextureVideoView(Context context) {
        super(context);
        init();
    }

    public TextureVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextureVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        super.setSurfaceTextureListener(surfaceTextureListener);
    }

    @Override
    public void setSurfaceTextureListener(SurfaceTextureListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void pause() {
        if(canPause()){
            mediaPlayer.pause();
            paused = true;
        }
    }

    @Override
    public void resume() {
        if(mediaPlayer != null){
            try {
                mediaPlayer.start();
            } catch (IllegalStateException e) {

            }
        }
    }

    private void runWhenSurfaceTextureAvailable(Runnable runnable) {
        if(onSurfaceTextureAvailableTasks != null){
            onSurfaceTextureAvailableTasks.add(runnable);
        } else {
            runnable.run();
        }
    }

    @Override
    public boolean canPause() {
        try {
            return mediaPlayer != null && mediaPlayer.isPlaying();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isPaused() {
        return mediaPlayer != null && paused;
    }

    public IOErrorListener getIoErrorListener() {
        return ioErrorListener;
    }

    public void setIoErrorListener(IOErrorListener ioErrorListener) {
        this.ioErrorListener = ioErrorListener;
    }

    public void setVideoPath(final String videoPath) {
        if(videoPath.equals(this.videoPath)){
            return;
        }

        this.videoPath = videoPath;
        if(ioErrorListener == null){
            throw new RuntimeException("set IoErrorListener before calling setVideoPath");
        }

        runWhenSurfaceTextureAvailable(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.setDataSource(videoPath);
                } catch (IOException e) {
                    ioErrorListener.onIOError(e);
                }
            }
        });
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void start(){
        runWhenSurfaceTextureAvailable(new Runnable() {
            @Override
            public void run() {
                if(videoPath == null){
                    throw new IllegalStateException("set videoPath before calling start");
                }

                try {
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                        }
                    });
                    mediaPlayer.prepareAsync();
                } catch (IllegalStateException e) {
                    mediaPlayer.start();
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
    }

    public void setOnCompletionListener(final MediaPlayer.OnCompletionListener onCompletionListener){
        runWhenSurfaceTextureAvailable(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.setOnCompletionListener(onCompletionListener);
            }
        });
    }
}
