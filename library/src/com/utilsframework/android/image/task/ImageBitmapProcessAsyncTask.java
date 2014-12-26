package com.utilsframework.android.image.task;

import android.graphics.Bitmap;
import android.net.Uri;
import com.utilsframework.android.file.IoUtils;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageBitmapProcessAsyncTask extends ImageProcessAsyncTask<Bitmap> {

    public ImageBitmapProcessAsyncTask(Uri outUri) {
        super(outUri);
    }

    @Override
    protected Uri doInBackground(Bitmap... params) {
        try {
            OutputStream outStream = new FileOutputStream(outImageUri.getPath());
            Bitmap inputBmp = params[0];

            if (inputBmp != null) {
                inputBmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            }

            IoUtils.closeSilently(outStream);
        } catch (Exception ex) {
            executionListener.onImageProcessedWithError(params[0]);
        }
        return outImageUri;
    }
}
