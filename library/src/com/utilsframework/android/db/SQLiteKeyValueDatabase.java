package com.utilsframework.android.db;

import android.content.Context;

/**
 * Created by CM on 12/26/2014.
 */
public class SQLiteKeyValueDatabase implements KeyValueDatabase {
    private static class KeyValue {
        @SQLIdField
        @SQLDataStoreField(fieldName = "key")
        String key;
        @SQLDataStoreField(fieldName = "value")
        String value;

        public KeyValue() {
        }
    }

    private DataStore<KeyValue> dataStore;
    private AsyncDataStore<KeyValue> asyncDataStore;

    public SQLiteKeyValueDatabase(Context context, String tableName) {
        dataStore = SQLiteDataStore.create(context, KeyValue.class, tableName);
        asyncDataStore = new AsyncDataStoreWrapper<KeyValue>(dataStore);
    }

    public SQLiteKeyValueDatabase(Context context) {
        this(context, null);
    }

    @Override
    public String get(String key) {
        KeyValue value = dataStore.getElementById(key);
        if (value != null) {
            return value.value;
        }

        return null;
    }

    @Override
    public void set(String key, String value) {
        if(value == null){
            dataStore.removeObjectWithId(key);
            return;
        }

        KeyValue keyValue = new KeyValue();
        keyValue.key = key;
        keyValue.value = value;
        dataStore.addOrReplace(keyValue);
    }

    @Override
    public void getAsync(String key, final OnResult<String> onResult) {
        asyncDataStore.getElementById(key, new OnResult<KeyValue>() {
            @Override
            public void onResult(KeyValue value) {
                onResult.onResult(value.value);
            }
        });
    }

    @Override
    public void setAsync(String key, String value, OnFinish onFinish) {
        if(value == null){
            asyncDataStore.removeObjectWithId(key, onFinish);
            return;
        }

        KeyValue keyValue = new KeyValue();
        keyValue.key = key;
        keyValue.value = value;
        asyncDataStore.addOrReplace(keyValue, onFinish);
    }

    @Override
    public void close() {
        dataStore.close();
    }
}
