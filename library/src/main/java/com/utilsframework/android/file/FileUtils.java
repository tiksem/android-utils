package com.utilsframework.android.file;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import com.utilsframework.android.threading.AbstractCancelable;
import com.utilsframework.android.threading.BackgroundLoopEvent;
import com.utilsframework.android.threading.Cancelable;
import com.utilsframework.android.threading.ResultLoop;
import com.utilsframework.android.threading.Tasks;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * User: Tikhonenko.S
 * Date: 07.07.14
 * Time: 14:44
 */
public final class FileUtils {
    public static String removeExtension(String filename) {
        return filename.replaceFirst("[.][^.]+$", "");
    }

    public static String getExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }

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

        return new AbstractCancelable() {
            @Override
            protected void doCancel(boolean mayInterruptIfRunning) {
                backgroundLoopEvent.stop();
            }
        };
    }

    public static Cancelable startFileSizeChangesDetecting(final OnFileSizeChanged onFileSizeChanged,
                                                           String filepath,
                                                           long maxWaitTime){
        return startFileSizeChangesDetecting(onFileSizeChanged, new File(filepath), maxWaitTime);
    }

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
    private static final String INDIVIDUAL_DIR_NAME = "uil-images";

    /**
     * Returns application cache directory. Cache directory will be created on SD card
     * <i>("/Android/data/[app_package_name]/cache")</i> if card is mounted and app has appropriate permission. Else -
     * Android defines cache directory on device's file system.
     *
     * @param context
     *            Application context
     * @return Cache {@link File directory}
     */
    public static final File getCacheDirectory(Context context) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {

        }
        return appCacheDir;
    }

    /**
     * Returns individual application cache directory (for only image caching from ImageLoader). Cache directory will be
     * created on SD card <i>("/Android/data/[app_package_name]/cache/uil-images")</i> if card is mounted and app has
     * appropriate permission. Else - Android defines cache directory on device's file system.
     *
     * @param context
     *            Application context
     * @return Cache {@link File directory}
     */
    public static final File getIndividualCacheDirectory(Context context) {
        File cacheDir = getCacheDirectory(context);
        File individualCacheDir = new File(cacheDir, INDIVIDUAL_DIR_NAME);
        if (!individualCacheDir.exists()) {
            if (!individualCacheDir.mkdir()) {
                individualCacheDir = cacheDir;
            }
        }
        return individualCacheDir;
    }

    /**
     * Returns specified application cache directory. Cache directory will be created on SD card by defined path if card
     * is mounted and app has appropriate permission. Else - Android defines cache directory on device's file system.
     *
     * @param context
     *            Application context
     * @param cacheDir
     *            Cache directory path (e.g.: "AppCacheDir", "AppDir/cache/images")
     * @return Cache {@link File directory}
     */
    public static final File getOwnCacheDirectory(Context context, String cacheDir) {
        File appCacheDir = null;
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cacheDir);
        }
        if (appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
            appCacheDir = context.getCacheDir();
        }
        return appCacheDir;
    }

    private static final File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {

            }
        }
        return appCacheDir;
    }

    private static final boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    public static File createTempFile(Context context, String fileName) {
        File outputDir = context.getCacheDir(); // context being the Activity pointer
        try {
            return File.createTempFile(removeExtension(fileName),
                    getExtension(fileName), outputDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateSDCard(Context context) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    private static class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection connection;
        private String filePath;

        public SingleMediaScanner(Context context, String filePath) {
            this.filePath = filePath;
            connection = new MediaScannerConnection(context, this);
            connection.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            connection.scanFile(filePath, null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            connection.disconnect();
        }

    }

    public interface OnFileScanned {
        void onFileScanned(String filePath);
    }

    public static void scanFile(Context context, final String filePath, final OnFileScanned onFileScanned) {
        new SingleMediaScanner(context, filePath) {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                super.onScanCompleted(path, uri);
                onFileScanned.onFileScanned(filePath);
            }
        };
    }
}
