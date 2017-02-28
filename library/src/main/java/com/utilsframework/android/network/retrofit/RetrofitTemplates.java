package com.utilsframework.android.network.retrofit;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitTemplates {
    private static final String REQUEST_LOGGING_TAG = "Retrofit";

    public static Retrofit.Builder generateRetrofitWithJackson(String baseUrl) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JacksonConverterFactory factory = JacksonConverterFactory.create(objectMapper);
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(factory);
    }

    public static Retrofit.Builder generateRetrofitWithJacksonAndLogging(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.i(REQUEST_LOGGING_TAG, message);
                    }
                });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = generateClientWithLogging();
        Retrofit.Builder builder = generateRetrofitWithJackson(baseUrl);
        builder.client(client.build());
        return builder;
    }

    public static OkHttpClient.Builder generateClientWithLogging() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.i(REQUEST_LOGGING_TAG, message);
                    }
                });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).
                addNetworkInterceptor(interceptor);
    }
}
