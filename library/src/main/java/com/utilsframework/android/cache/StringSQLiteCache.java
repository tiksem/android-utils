package com.utilsframework.android.cache;

import android.content.Context;
import com.utils.framework.collections.cache.AbstractCache;
import com.utilsframework.android.db.SQLiteDataStore;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by CM on 9/10/2015.
 */
public class StringSQLiteCache extends AbstractCache<String, String> {
    private SQLiteDataStore<CacheEntity> dataStore;
    private SQLiteDataStore<CacheEntityCount> countDataStore;
    private ReadWriteLock readWriteLock;

    public StringSQLiteCache(Context context, String tableName, int maxRecords) {
        dataStore = SQLiteDataStore.create(context, CacheEntity.class, tableName);
        countDataStore = SQLiteDataStore.create(context, CacheEntityCount.class, tableName + "_count");
        if (countDataStore.getElements().isEmpty()) {
            countDataStore.add(new CacheEntityCount(maxRecords));
        }

        readWriteLock = new ReentrantReadWriteLock();
    }

    @Override
    public String get(String key) {
        try {
            readWriteLock.readLock().lock();

            CacheEntity value = getEntity(key);
            if (value == null) {
                return null;
            }

            return value.value;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private CacheEntity getEntity(String key) {
        return dataStore.getElementByField("key", key);
    }

    @Override
    public String put(String key, String value) {
        try {
            readWriteLock.writeLock().lock();
            CacheEntityCount entityCount = countDataStore.getFirstElement();
            while (entityCount.count >= entityCount.max) {
                dataStore.removeLastRecord();
                countDataStore.changeFieldOfAllRecords("count", -1);
                entityCount.count--;
            }

            CacheEntity entity = getEntity(key);
            if (entity != null) {
                String returnValue = entity.value;
                entity.value = value;
                dataStore.addOrReplace(entity);
                return returnValue;
            } else {
                countDataStore.changeFieldOfAllRecords("count", 1);
                dataStore.add(new CacheEntity(key, value));
                return null;
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
