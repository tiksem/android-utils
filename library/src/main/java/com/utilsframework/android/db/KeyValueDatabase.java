package com.utilsframework.android.db;

/**
 * Created by CM on 12/26/2014.
 */
public interface KeyValueDatabase {
    String get(String key);
    void set(String key, String value);
    void getAsync(String key, OnResult<String> onResult);
    void setAsync(String key, String value, OnFinish onFinish);
    void close();
}
