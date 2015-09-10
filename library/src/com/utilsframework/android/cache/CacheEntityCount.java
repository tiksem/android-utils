package com.utilsframework.android.cache;

import com.utilsframework.android.db.SQLDataStoreField;

/**
 * Created by CM on 9/11/2015.
 */
public class CacheEntityCount {
    @SQLDataStoreField(fieldName = "count")
    public int count;
    @SQLDataStoreField(fieldName = "max")
    public int max;

    public CacheEntityCount(int max) {
        this.max = max;
    }

    public CacheEntityCount() {
    }
}
