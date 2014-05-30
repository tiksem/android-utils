package com.utilsframework.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * User: Tikhonenko.S
 * Date: 30.05.14
 * Time: 15:40
 */
public class SQLiteDataStore<T> extends AbstractSQLiteDataStore<T>{
    private static final String DATABASE_NAME = "SQLiteDataStore";

    protected SQLiteDataStore(SQLiteDatabase database, Class<T> aClass) {
        super(database, aClass);
    }

    public static <T> SQLiteDataStore<T> create(SQLiteDatabase database, Class<T> aClass){
        return new SQLiteDataStore<T>(database, aClass);
    }

    public static <T> SQLiteDataStore<T> create(Context context, Class<T> aClass) {
        SQLiteDatabase database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        return create(database, aClass);
    }

    @Override
    protected T createObject() {
        try {
            return getTableEntityClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getTableName() {
        return getTableEntityClass().getCanonicalName();
    }
}
