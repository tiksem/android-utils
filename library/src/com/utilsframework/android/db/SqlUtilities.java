package com.utilsframework.android.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.utilsframework.android.string.Strings;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: Tikhonenko.S
 * Date: 21.10.13
 * Time: 17:58
 * To change this template use File | Settings | File Templates.
 */
public class SqlUtilities {
    public static final String CREATE = "CREATE TABLE IF NOT EXISTS ";
    public static final String STRING_TYPE = "TEXT";
    public static final String INT_TYPE = "INTEGER";
    public static final String FLOAT_TYPE = "REAL";
    public static final String PRIMARY_KEY = "PRIMARY KEY";
    public static final String AUTOINCREMENT = "AUTOINCREMENT";

    public static String getCreateTableQuery(CharSequence[] fieldDeclarations, String tableName){
        CharSequence fields = Strings.join(", ", fieldDeclarations);
        StringBuilder result = new StringBuilder();
        result.append(SqlUtilities.CREATE);
        result.append(tableName);
        result.append('(');
        result.append(fields);
        result.append(");");

        return result.toString();
    }

    public static void createTableIfNotExists(CharSequence[] fieldDeclarations, String tableName,
                                              SQLiteDatabase database){
        String query = getCreateTableQuery(fieldDeclarations, tableName);
        database.execSQL(query);
    }

    public static String createIdType(String sqlType){
        sqlType += " " + PRIMARY_KEY + " ";
        if(sqlType.equals(INT_TYPE)){
            sqlType += AUTOINCREMENT + " ";
        }
        return sqlType;
    }

    public static String createIdType(Class type){
        String sqlType = getSqlTypeName(type);
        return createIdType(sqlType);
    }

    public static boolean isInt(Class aClass){
        return aClass == int.class || aClass == long.class;
    }

    public static boolean isFloat(Class aClass){
        return aClass == float.class || aClass == double.class;
    }

    public static boolean isString(Class aClass){
        return aClass == String.class;
    }

    public static String getSqlTypeName(Class aClass){
        if(isString(aClass)){
            return STRING_TYPE;
        } else if(isInt(aClass)) {
            return INT_TYPE;
        } else if(isFloat(aClass)) {
            return FLOAT_TYPE;
        }

        throw new IllegalArgumentException(aClass.getCanonicalName() + " is not supported");
    }

    public static String getSqlTypeName(Field field){
        Class aClass = field.getType();
        return getSqlTypeName(aClass);
    }

    public static String getSqlValue(Object object){
        String value = object.toString();
        if(object instanceof CharSequence){
            value = value.replace("'", "\\'");
            value = '\'' + value + '\'';
        }

        return value;
    }

    public static void putContentValue(ContentValues contentValues, String fieldName, Object value){
        putContentValue(contentValues, fieldName, value, null);
    }

    public static void putContentValue(ContentValues contentValues, String fieldName, Object value, Class type){
        if(value == null) {
            contentValues.putNull(fieldName);
            return;
        }

        if (type == null) {
            type = value.getClass();
        }

        if(int.class.isAssignableFrom(type)) {
            contentValues.put(fieldName, (Integer) value);
        } else if(long.class.isAssignableFrom(type)) {
            contentValues.put(fieldName, (Long)value);
        } else if(CharSequence.class.isAssignableFrom(type)) {
            contentValues.put(fieldName, value.toString());
        } else if(float.class.isAssignableFrom(type)) {
            contentValues.put(fieldName, (Float)value);
        } else if(double.class.isAssignableFrom(type)) {
            contentValues.put(fieldName, (Double)value);
        } else {
            throw new IllegalArgumentException("type is not supported");
        }
    }

    static Object getColumnValueFromCursor(Cursor cursor, Class aClass, int columnIndex){
        if (isInt(aClass)) {
            return cursor.getInt(columnIndex);
        } else if(isFloat(aClass)) {
            return cursor.getFloat(columnIndex);
        } else if(isString(aClass)) {
            return cursor.getString(columnIndex);
        }

        throw new IllegalArgumentException(aClass.getCanonicalName() + " is not supported");
    }
}
