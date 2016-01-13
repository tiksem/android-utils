package com.utilsframework.android.cache;

import com.utilsframework.android.db.SQLDataStoreField;
import com.utilsframework.android.db.SQLIdField;

/**
 * Created by CM on 9/11/2015.
 */
public class CacheEntity {
    @SQLIdField
    @SQLDataStoreField(fieldName = "id")
    public Integer id;
    @SQLDataStoreField(fieldName = "key", unique = true)
    public String key;
    @SQLDataStoreField(fieldName = "value")
    public String value;

    public CacheEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public CacheEntity() {
    }
}
