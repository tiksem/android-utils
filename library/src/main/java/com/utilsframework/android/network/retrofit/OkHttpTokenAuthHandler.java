package com.utilsframework.android.network.retrofit;

import android.content.Context;

import com.utilsframework.android.sp.SharedPreferencesMap;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;

public class OkHttpTokenAuthHandler {
    public static final String TOKEN = "token";

    private final SharedPreferencesMap preferencesMap;
    private final AuthenticationInterceptor authenticationInterceptor;

    public OkHttpTokenAuthHandler(OkHttpClient.Builder builder, Context context) {
        preferencesMap = new SharedPreferencesMap(context);
        authenticationInterceptor = new AuthenticationInterceptor(getAuthToken());
        builder.addInterceptor(authenticationInterceptor);
    }

    private String getAuthToken() {
        return preferencesMap.getString(TOKEN);
    }

    public void login(String token) {
        preferencesMap.putString(TOKEN, token);
        authenticationInterceptor.setAuthToken(getAuthToken());
    }

    public void logout() {
        preferencesMap.remove(TOKEN);
        authenticationInterceptor.setAuthToken(null);
    }

    public boolean isLoggedIn() {
        return getAuthToken() != null;
    }
}
