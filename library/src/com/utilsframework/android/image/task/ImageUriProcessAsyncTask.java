package com.utilsframework.android.image.task;

import android.net.Uri;
import com.utilsframework.android.file.IoUtils;

import java.io.*;

public class ImageUriProcessAsyncTask extends ImageProcessAsyncTask<Uri> {

    public ImageUriProcessAsyncTask(Uri outUri) {
        super(outUri);
    }

    @Override
    protected Uri doInBackground(Uri... params) {
        try {
            InputStream inStream = new FileInputStream(params[0].getPath());
            OutputStream outStream = new FileOutputStream(outImageUri.getPath());

            IoUtils.copyStream(inStream, outStream);

            IoUtils.closeSilently(inStream);
            IoUtils.closeSilently(outStream);
        } catch (IOException ex) {
            executionListener.onImageProcessedWithError(params[0]);
        }
        return outImageUri;
    }
}
