package com.utilsframework.android.network;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import com.utils.framework.io.Network;
import com.utils.framework.io.ProgressListener;
import com.utilsframework.android.file.FileUtils;
import com.utilsframework.android.threading.Threading;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by stykhonenko on 03.12.15.
 */
public abstract class DownloadFileService extends Service {
    private static final String URL = "URL";
    private static final String FILE_NAME = "FILE_NAME";
    private static final String COMMAND = "command";
    private static final int DOWNLOAD_FILE = 1;
    private static final int PROGRESS_UPDATE_DELAY = 400;

    private AsyncRequestExecutorManager requestManager;
    private ExecutorService executor;
    private List<String> downloadingFiles = new ArrayList<>();
    private Handler handler;
    private NotificationManager notificationManager;

    protected static Intent createStartIntent(String url, String saveFileName,
                                            Context context,
                                            Class<? extends DownloadFileService> aClass) {
        Intent intent = new Intent(context, aClass);
        intent.putExtra(URL, url);
        intent.putExtra(FILE_NAME, saveFileName);
        intent.putExtra(COMMAND, DOWNLOAD_FILE);
        return intent;
    }

    protected abstract void startForeground(Intent intent, String url, String saveFileName, int fileIndex);

    private void deleteFileAsync(final String saveFileName) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                new File(saveFileName).delete();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!downloadingFiles.isEmpty()) {
            requestManager.execute(new Threading.Task<IOException, Object>() {
                @Override
                public Object runOnBackground() throws IOException {
                    for (String file : downloadingFiles) {
                        new File(file).delete();
                    }

                    return null;
                }

                @Override
                public void onAfterCompleteOrCancelled() {
                    requestManager.cancelAll();
                    executor.shutdown();
                }
            });
        } else {
            requestManager.cancelAll();
            executor.shutdown();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executor = Executors.newSingleThreadExecutor();
        requestManager = new AsyncRequestExecutorManager(executor);
        handler = new Handler();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int command = intent.getIntExtra(COMMAND, DOWNLOAD_FILE);
        if (command == DOWNLOAD_FILE) {
            downloadFile(intent);
        } else {
            throw new IllegalStateException("Unknown command");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void downloadFile(final Intent intent) {
        final String url = intent.getStringExtra(URL);
        final String saveFileName = intent.getStringExtra(FILE_NAME);
        downloadingFiles.add(saveFileName);
        final int fileIndex = downloadingFiles.size();

        requestManager.execute(new Threading.Task<IOException, Object>() {
            @Override
            public Object runOnBackground() throws IOException {
                startForeground(intent, url, saveFileName, fileIndex);
                Network.downloadFile(url, saveFileName, new ProgressListener() {
                    long lastProgressUpdate = 0;

                    @Override
                    public void onProgressChanged(final int progress, final int max) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastProgressUpdate > PROGRESS_UPDATE_DELAY) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    DownloadFileService.this.onProgressChanged(fileIndex, max, progress);
                                }
                            });
                            lastProgressUpdate = currentTime;
                        }
                    }
                });
                return null;
            }

            @Override
            public void onCancelled(Object o, IOException error) {
                deleteFileAsync(saveFileName);
            }

            @Override
            public void onComplete(Object o, IOException error) {
                if (error != null) {
                    deleteFileAsync(saveFileName);
                    onHandleError(error);
                } else {
                    onFileDownloaded(saveFileName, intent);
                    if (shouldScanFileWithMediaScanner()) {
                        FileUtils.scanFile(DownloadFileService.this, saveFileName, new FileUtils.OnFileScanned() {
                            @Override
                            public void onFileScanned(String filePath) {
                                DownloadFileService.this.onFileScanned(saveFileName, intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onAfterCompleteOrCancelled() {
                downloadingFiles.remove(saveFileName);
                notificationManager.cancel(fileIndex);
                if (downloadingFiles.isEmpty()) {
                    stopForeground(true);
                }
            }
        });
    }

    protected void onFileDownloaded(String saveFileName, Intent intent) {

    }

    protected void onFileScanned(String saveFileName, Intent intent) {

    }

    protected boolean shouldScanFileWithMediaScanner() {
        return true;
    }

    protected abstract void onHandleError(IOException error);
    protected abstract void onProgressChanged(int fileIndex, int max, int progress);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
