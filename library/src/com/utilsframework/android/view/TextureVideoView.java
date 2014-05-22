package com.utilsframework.android.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import com.utilsframework.android.IOErrorListener;
import com.utilsframework.android.Pauseable;
import com.utilsframework.android.media.MediaPlayerProvider;
import com.utilsframework.android.threading.Tasks;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Tikhonenko.S
 * Date: 02.04.14
 * Time: 18:32
 */
public class TextureVideoView extends TextureView implements IVideoView{
    public enum ScaleType {
        CENTER_CROP, TOP, BOTTOM
    }

    private MediaPlayer mediaPlayer;
    private boolean paused = false;
    private Deque<Runnable> onSurfaceTextureAvailableTasks = new ArrayDeque<Runnable>();
    private String videoPath;
    private IOErrorListener ioErrorListener;
    private boolean playBackCompleted = true;
    private Set<MediaPlayer.OnCompletionListener> onCompletionListeners =
            new HashSet<MediaPlayer.OnCompletionListener>();
    private int videoHeight, videoWidth;
    private ScaleType scaleType = ScaleType.CENTER_CROP;

    private SurfaceTextureListener surfaceTextureListener = new SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    for(MediaPlayer.OnCompletionListener listener : onCompletionListeners){
                        listener.onCompletion(mp);
                    }

                    playBackCompleted = true;
                }
            });

            Tasks.executeAndClearQueue(onSurfaceTextureAvailableTasks);
            onSurfaceTextureAvailableTasks = null;

            mediaPlayer.setSurface(new Surface(surface));
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            onSurfaceTextureAvailableTasks = new ArrayDeque<Runnable>();
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
        try {
            mediaPlayer.pause();
            paused = true;
        } catch (IllegalStateException e) {

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
            boolean value =  mediaPlayer != null && !playBackCompleted && !paused;
            return value;
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

    @Override
    public void addOnCompletionListener(MediaPlayer.OnCompletionListener onCompleteListener) {
        onCompletionListeners.add(onCompleteListener);
    }

    @Override
    public void removeOnCompletionListener(MediaPlayer.OnCompletionListener onCompleteListener) {
        onCompletionListeners.remove(onCompleteListener);
    }

    @Override
    public void start(){
        runWhenSurfaceTextureAvailable(new Runnable() {
                private boolean isPreparing = false;

            @Override
            public void run() {
                if(videoPath == null){
                    throw new IllegalStateException("set videoPath before calling start");
                }

                try {
                    isPreparing = true;

                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                            playBackCompleted = false;
                            isPreparing = false;
                        }
                    });
                    mediaPlayer.setOnVideoSizeChangedListener(
                            new MediaPlayer.OnVideoSizeChangedListener() {
                                @Override
                                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                    videoWidth = width;
                                    videoHeight = height;
                                    updateTextureViewSize();
                                }
                            }
                    );

                    mediaPlayer.prepareAsync();
                } catch (IllegalStateException e) {
                    if (!isPreparing) {
                        mediaPlayer.start();
                        playBackCompleted = false;
                    }
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void updateTextureViewSize() {
        float viewWidth = getWidth();
        float viewHeight = getHeight();

        float scaleX = 1.0f;
        float scaleY = 1.0f;

        if (videoWidth > viewWidth && videoHeight > viewHeight) {
            scaleX = videoWidth / viewWidth;
            scaleY = videoHeight / viewHeight;
        } else if (videoWidth < viewWidth && videoHeight < viewHeight) {
            scaleY = viewWidth / videoWidth;
            scaleX = viewHeight / videoHeight;
        } else if (viewWidth > videoWidth) {
            scaleY = (viewWidth / videoWidth) / (viewHeight / videoHeight);
        } else if (viewHeight > videoHeight) {
            scaleX = (viewHeight / videoHeight) / (viewWidth / videoWidth);
        }

        scaleX *= viewWidth / videoWidth;
        scaleY *= viewWidth / videoWidth;

        // Calculate pivot points, in our case crop from center
        int pivotPointX;
        int pivotPointY;

        switch (scaleType) {
            case TOP:
                pivotPointX = 0;
                pivotPointY = 0;
                break;
            case BOTTOM:
                pivotPointX = (int) (viewWidth);
                pivotPointY = (int) (viewHeight);
                break;
            case CENTER_CROP:
                pivotPointX = (int) (viewWidth / 2);
                pivotPointY = (int) (viewHeight / 2);
                break;
            default:
                pivotPointX = (int) (viewWidth / 2);
                pivotPointY = (int) (viewHeight / 2);
                break;
        }

        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY, pivotPointX, pivotPointY);

        setTransform(matrix);
    }

    @Override
    public void stop() {
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
    }
}
