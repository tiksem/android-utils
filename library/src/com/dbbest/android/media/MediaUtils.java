package com.dbbest.android.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import com.dbbest.android.threading.*;

import java.io.File;
import java.io.IOException;

/**
 * User: Tikhonenko.S
 * Date: 24.12.13
 * Time: 18:20
 */
public class MediaUtils {
    public interface DurationResult{
        void onResult(boolean success, int duration);
    }

    public static Cancelable tryGetMediaFileDuration(
            final DurationResult onFinish,
            final Context context,
            final String filePath,
            long maxWaitTime){
        final BackgroundLoopEvent backgroundLoopEvent = Tasks.waitForResult(new ResultLoop<Integer>() {
            private MediaPlayer mediaPlayer;

            @Override
            public boolean resultIsReady() {
                mediaPlayer = MediaPlayer.create(context, Uri.fromFile(new File(filePath)));
                return mediaPlayer != null;
            }

            @Override
            public Integer getResult() {
                return mediaPlayer.getDuration();
            }

            @Override
            public void handleResult(Integer result) {
                onFinish.onResult(true, result);
            }

            @Override
            public void onTimeIsUp() {
                onFinish.onResult(false, -1);
            }
        });

        backgroundLoopEvent.setMaxRunningTime(maxWaitTime);

        return new Cancelable() {
            @Override
            public void cancel() {
                backgroundLoopEvent.setOnStop(null);
                backgroundLoopEvent.stop();
            }
        };
    }
}
