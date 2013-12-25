package com.dbbest.android.file;

import com.dbbest.android.threading.BackgroundLoopEvent;
import com.dbbest.android.threading.Cancelable;
import com.dbbest.android.threading.ResultLoop;
import com.dbbest.android.threading.Tasks;

import java.io.File;

/**
 * User: Tikhonenko.S
 * Date: 25.12.13
 * Time: 18:00
 */
public class FileSystemUtils {
    public static Cancelable startFileSizeChangesDetecting(final OnFileSizeChanged onFileSizeChanged,
                                                           final File file,
                                                           long maxWaitTime){
        final long fileLengthBefore = file.length();
        final BackgroundLoopEvent backgroundLoopEvent = Tasks.waitForResult(new ResultLoop<Object>() {
            @Override
            public boolean resultIsReady() {
                return fileLengthBefore != file.length();
            }

            @Override
            public Object getResult() {
                return null;
            }

            @Override
            public void handleResult(Object result) {
                onFileSizeChanged.onFileSizeChanged();
            }

            @Override
            public void onTimeIsUp() {

            }
        });

        backgroundLoopEvent.setMaxRunningTime(maxWaitTime);

        return new Cancelable() {
            @Override
            public void cancel() {
                backgroundLoopEvent.stop();
            }
        };
    }

    public static Cancelable startFileSizeChangesDetecting(final OnFileSizeChanged onFileSizeChanged,
                                                           String filepath,
                                                           long maxWaitTime){
        return startFileSizeChangesDetecting(onFileSizeChanged, new File(filepath), maxWaitTime);
    }
}
