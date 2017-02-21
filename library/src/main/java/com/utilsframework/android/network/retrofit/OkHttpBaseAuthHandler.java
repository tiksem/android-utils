package com.utilsframework.android.network.retrofit;

import android.content.Context;

import com.utilsframework.android.sp.SharedPreferencesMap;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;

public class OkHttpBaseAuthHandler {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    private final SharedPreferencesMap preferencesMap;
    private final AuthenticationInterceptor authenticationInterceptor;

    public OkHttpBaseAuthHandler(OkHttpClient.Builder builder, Context context) {
        preferencesMap = new SharedPreferencesMap(context);
        authenticationInterceptor = new AuthenticationInterceptor(getAuthToken());
        builder.addInterceptor(authenticationInterceptor);
    }

    private String getAuthToken() {
        String username = preferencesMap.getString(USERNAME);
        if (username == null) {
            return null;
        }

        String password = preferencesMap.getString(PASSWORD);
        if (password == null) {
            return null;
        }

        return Credentials.basic(username, password);
    }

    public void login(String password, String username) {
        preferencesMap.putString(PASSWORD, password);
        preferencesMap.putString(USERNAME, username);
        authenticationInterceptor.setAuthToken(getAuthToken());
    }

    public void logout() {
        preferencesMap.remove(PASSWORD);
        preferencesMap.remove(USERNAME);
        authenticationInterceptor.setAuthToken(null);
    }
}
