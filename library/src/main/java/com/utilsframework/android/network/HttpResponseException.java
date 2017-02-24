package com.utilsframework.android.network;

import java.io.IOException;

public class HttpResponseException extends IOException {
    private int responseCode;
    private Object data;
    private String message;

    public HttpResponseException(int responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
