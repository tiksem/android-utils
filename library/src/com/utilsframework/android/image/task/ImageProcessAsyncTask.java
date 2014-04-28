package com.utilsframework.android.image.task;

import android.net.Uri;
import android.os.AsyncTask;
import com.utilsframework.android.image.listener.ImageProcessResult;

public abstract class ImageProcessAsyncTask<T> extends AsyncTask<T, Void, Uri> {
    protected static final String TAG = ImageProcessAsyncTask.class.getSimpleName();

    protected Uri outImageUri;
    protected ImageProcessResult executionListener;

    public ImageProcessAsyncTask(Uri outUri) {
        this.outImageUri = outUri;
    }

    public void setImageProcessResultListener(ImageProcessResult listener) {
        executionListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        executionListener.onImageProcessedStart();
    }

    @Override
    protected void onPostExecute(Uri uri) {
        super.onPostExecute(uri);

        executionListener.onImageProcessedSuccess(uri);
    }
}
