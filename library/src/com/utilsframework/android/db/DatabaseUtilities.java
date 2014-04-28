package com.utilsframework.android.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Tikhonenko.S on 10.10.13.
 */
public class DatabaseUtilities {
    public static boolean contains(SQLiteDatabase database, String tableName, String keyName,
                                   Object value){
        String valueAsString = value.toString();
        if(value instanceof CharSequence){
            valueAsString = '\'' + valueAsString + '\'';
        }

        String where = keyName + " = " + valueAsString;
        Cursor cursor = database.query(tableName, new String[]{keyName}, where, null, null, null,
                null);
        return cursor.getCount() > 0;
    }
}
