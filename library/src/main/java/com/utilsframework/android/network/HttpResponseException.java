package com.utilsframework.android.network;

import java.io.IOException;

import retrofit2.Response;

public class HttpResponseException extends IOException {
    private Response response;
    private Object data;
    private String message;

    public HttpResponseException(Response response, String message) {
        super(message);
        this.response = response;
        this.message = message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Response getResponse() {
        return response;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
