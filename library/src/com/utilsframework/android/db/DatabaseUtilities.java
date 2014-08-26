package com.utilsframework.android.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.utils.framework.Reflection;
import com.utilsframework.android.string.Strings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final String ID_TABLE_NAME = "__id";

    private static void throwTypeNotSupported(Class aClass){
        throw new IllegalArgumentException("The type '" +
                aClass.getName() + "' is not supported. The supported types are: " +
                "String, long, double");
    }

    public static String toDatabaseTypeName(Class aClass, int maxStringLength){
        if(aClass == String.class){
            return "TEXT(" + maxStringLength + ")";
        } else if(aClass == long.class){
            return "NOT NULL INTEGER";
        } else if(aClass == double.class){
            return "NOT NULL DOUBLE";
        }

        throwTypeNotSupported(aClass);
        return null;
    }

    public static class CreateTableForClassParams{
        public SQLiteDatabase database;
        public String tableName = "ExampleTableName";
        public int maxStringLength = 50;
        public Class aClass;
        public boolean ifNotExists = true;
        public boolean includeId = false;
        public List<String> outColumnNames;

        public void check(){
            if(aClass == null || database == null){
                throw new NullPointerException();
            }
        }
    }

    public static List<Field> createTableForClass(CreateTableForClassParams params){
        params.check();

        Class aClass = params.aClass;
        int maxStringLength = params.maxStringLength;
        SQLiteDatabase database = params.database;
        String tableName = params.tableName;
        String ifNotExists = params.ifNotExists ? "if not exists" : "";

        List<Field> fields = Reflection.getAllFieldsOfClass(aClass);
        List<Object> fieldsDefinitions = new ArrayList<Object>(fields.size());
        for(Field field : fields){
            String fieldName = field.getName();
            if(params.outColumnNames != null){
                params.outColumnNames.add(fieldName);
            }

            StringBuilder definition = new StringBuilder(fieldName);

            definition.append(" ");
            Class objectClass = field.getType();
            String type = DatabaseUtilities.toDatabaseTypeName(objectClass, maxStringLength);
            definition.append(" ");
            definition.append(type);
            fieldsDefinitions.add(definition);
        }

        if(params.includeId){
            String definition = ID_TABLE_NAME + " INTEGER PRIMARY KEY ASC";
            fieldsDefinitions.add(definition);
        }

        String definitions = Strings.joinObjects(", ", fieldsDefinitions).toString();
        String sql = "create table " + ifNotExists + " " + tableName + " (" + definitions + ")";
        database.execSQL(sql);

        return fields;
    }

    public static String fieldValueAsString(Object fieldValue){
        String fieldValueAsString = fieldValue.toString();
        if(fieldValue instanceof CharSequence){
            fieldValueAsString = "'" + fieldValueAsString + "'";
        }

        return fieldValueAsString;
    }

    public static <T> T convertCursorToObject(Cursor cursor, Class<T> tClass){
        try {
            if(cursor.getCount() <= 0){
                return null;
            }

            T object = Reflection.createObjectOfClass(tClass);
            List<Field> fields = Reflection.getAllFieldsOfClass(tClass);
            for(Field field : fields){
                String columnName = field.getName();
                int columnIndex = cursor.getColumnIndexOrThrow(columnName);
                Class type = field.getType();
                Object value = null;

                if(type == String.class){
                    value = cursor.getString(columnIndex);
                } else if(type == long.class) {
                    value = cursor.getLong(columnIndex);
                } else if(type == double.class) {
                    value = cursor.getDouble(columnIndex);
                } else {
                    throwTypeNotSupported(tClass);
                }

                Reflection.setValueOfField(object, field, value);
            }

            return object;
        } catch (Throwable throwable) {
            return null;
        }
    }

    public static <T> T selectObjectByField(SQLiteDatabase database, String tableName,
                                            String fieldName, Object fieldValue, String[] columnNames,
                                            Class<T> tClass){
        String fieldValueAsString = fieldValueAsString(fieldValue);
        String selection = fieldName + " = " + fieldValueAsString;
        Cursor cursor = database.query(tableName, columnNames, selection, null, null, null, null, null);
        return convertCursorToObject(cursor, tClass);
    }

    public static <T> T selectObjectById(SQLiteDatabase database, String tableName,
                                         int fieldValue, String[] columnNames,
                                         Class<T> tClass){
        return selectObjectByField(database, tableName, ID_TABLE_NAME, fieldValue, columnNames, tClass);
    }

    public static List<String> getObjectFieldValuesAsStringList(Object object){
        List<Field> fields = Reflection.getAllFields(object);
        List<String> result = new ArrayList<String>(fields.size());
        for(Field field : fields){
            Object value = Reflection.getValueOfField(object, field);
            String stringValue = value.toString();
            if(field.getType() == String.class){
                stringValue = "'" + stringValue + "'";
            }

            result.add(stringValue);
        }

        return result;
    }

    public static int getIdFromCursor(Cursor cursor){
        int columnIndex = cursor.getColumnIndexOrThrow(ID_TABLE_NAME);
        return cursor.getInt(columnIndex);
    }

    public static <T> Map<Integer,T> cursorToMapWithIdKey(Cursor cursor, Class<T> tClass){
        Map<Integer, T> result = new HashMap<Integer, T>();

        int count = cursor.getCount();
        cursor.moveToFirst();
        for(int i = 0; i < count; i++){
            T object = convertCursorToObject(cursor, tClass);
            int id = getIdFromCursor(cursor);
            cursor.moveToNext();
            result.put(id, object);
        }

        return result;
    }

    public static void deleteObjectWithId(SQLiteDatabase database, String tableName, int id){
        String where = ID_TABLE_NAME + "=" + id;
        database.delete(tableName, where, null);
    }
}
