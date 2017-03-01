package com.utilsframework.android.network.retrofit;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestBodies {
    public static RequestBody fromString(String string) {
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }

    public static RequestBody image(File file) {
        return RequestBody.create(MediaType.parse("image/*"), file);
    }

    public static MultipartBody.Part imagePartFromFilePath(String name, String filePath) {
        MultipartBody.Part coverPart = null;
        if (filePath != null) {
            File file = new File(filePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),
                    file);
            return MultipartBody.Part.createFormData(name,
                    file.getName(), requestFile);
        }

        return null;
    }
}
