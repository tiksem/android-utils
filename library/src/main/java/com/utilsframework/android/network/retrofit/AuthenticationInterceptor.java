package com.utilsframework.android.network.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private String authToken;
    private String headerKey = "Authorization";

    public AuthenticationInterceptor(String token) {
        this.authToken = token;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder();
        if (authToken != null) {
            builder.header(headerKey, authToken);
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}