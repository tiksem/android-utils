package com.utilsframework.android.network.retrofit;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RequestBodies {
    public static RequestBody fromString(String string) {
        return RequestBody.create(MediaType.parse("text/plain"), string);
    }

    public static RequestBody image(File file) {
        return RequestBody.create(MediaType.parse("image/*"), file);
    }
}
