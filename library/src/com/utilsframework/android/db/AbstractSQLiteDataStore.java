package com.utilsframework.android.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Tikhonenko.S on 18.09.13.
 */
public abstract class AbstractSQLiteDataStore<T> implements DataStore<T> {

    private String idColumnName;
    private Field idField;
    private String tableName;
    private SQLiteDatabase database;
    private Class<T> tClass;
    private Map<String,Field> dataBaseMapping = new LinkedHashMap<String, Field>();

    private String getCreateTableQuery(List<CharSequence> fieldDeclarations){
        return SqlUtilities.getCreateTableQuery(fieldDeclarations, tableName);
    }

    private void createTableIfNotExists(){
        Field[] fields = tClass.getDeclaredFields();
        List<CharSequence> fieldDeclarations = new ArrayList<CharSequence>();

        for(Field field : fields){
            field.setAccessible(true);

            SQLDataStoreField sqlDataStoreField = field.getAnnotation(SQLDataStoreField.class);
            if (sqlDataStoreField != null) {
                String fieldName = sqlDataStoreField.fieldName();

                if(fieldName.isEmpty()){
                    throw new IllegalArgumentException("each field, which does have SQLDataStoreField annotation, " +
                            "should have got non-empty fieldName param declaration");
                }

                dataBaseMapping.put(fieldName, field);
                String typeName = SqlUtilities.getSqlTypeName(field);
                SQLIdField sqlIdField = field.getAnnotation(SQLIdField.class);
                if (sqlIdField != null) {
                    if(idColumnName != null){
                        throw new IllegalStateException("multiple SQLIdField declaration");
                    }

                    String type = sqlIdField.type();
                    if (type.isEmpty()) {
                        typeName = SqlUtilities.createIdType(typeName);
                    } else {
                        typeName = type;
                    }

                    idColumnName = fieldName;
                    idField = field;
                }

                StringBuilder fieldDeclaration = new StringBuilder();
                fieldDeclaration.append(fieldName);
                fieldDeclaration.append(" ");
                fieldDeclaration.append(typeName);

                if(typeName.equals(SqlUtilities.STRING_TYPE)){
                    int maxStringLength = sqlDataStoreField.maxStringLength();

                    if (maxStringLength > 0) {
                        fieldDeclaration.append('(');
                        fieldDeclaration.append(maxStringLength);
                        fieldDeclaration.append(')');
                    }
                }

                fieldDeclarations.add(fieldDeclaration);
            }
        }

        if(idColumnName == null){
            throw new IllegalStateException("SQLIdField must be declared in " +
                    tClass.getCanonicalName());
        }

        String query = getCreateTableQuery(fieldDeclarations);
        database.execSQL(query);
    }

    protected AbstractSQLiteDataStore(SQLiteDatabase database, Class<T> tClass) {
        this.database = database;
        this.tClass = tClass;
        tableName = getTableName();

        if(tableName == null || tableName.equals("")){
            throw new IllegalArgumentException("getTableName returns empty string");
        }

        createTableIfNotExists();
    }

    private T objectFromCursor(Cursor cursor){
        if(cursor.isAfterLast()){
            return null;
        }

        T result = createObject();

        for(int i = 0; i < cursor.getColumnCount(); i++){
            String columnName = cursor.getColumnName(i);

            Field field = dataBaseMapping.get(columnName);
            Class fieldClass = field.getType();
            Object value = SqlUtilities.getColumnValueFromCursor(cursor, fieldClass, i);

            try {
                field.set(result, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    @Override
    public T getElementById(Object id) {
        return getElementByField(idColumnName, id);
    }

    private String getSelectByFieldQuery(String fieldName, Object value){
        return "SELECT * FROM " + tableName + " WHERE " + fieldName + "=" + value;
    }

    public void putContentValue(ContentValues contentValues, String fieldName, Object value){
        Class type = dataBaseMapping.get(fieldName).getType();
        SqlUtilities.putContentValue(contentValues, fieldName, value, type);
    }

    private ContentValues getContentValues(T object, Field exclude){
        ContentValues contentValues = new ContentValues(dataBaseMapping.size());
        for(Map.Entry<String, Field> entry : dataBaseMapping.entrySet()){
            String fieldName = entry.getKey();
            try {
                Field field = entry.getValue();
                if(field == exclude){
                    continue;
                }

                Object fieldValue = field.get(object);
                putContentValue(contentValues, fieldName, fieldValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return contentValues;
    }

    private ContentValues getContentValues(T object) {
        return getContentValues(object, null);
    }

    private Cursor executeSelectByFieldQuery(String fieldName, Object value){
        if(value instanceof CharSequence){
            value = '\'' + value.toString() + '\'';
        }

        String query = getSelectByFieldQuery(fieldName, value);
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    private void checkFieldName(String fieldName, Object value){
        Field field = dataBaseMapping.get(fieldName);

        if(field == null){
            throw new IllegalArgumentException(tClass.getCanonicalName() + " " +
                    "doesn't have '" + fieldName + "' field");
        }
    }

    @Override
    public T getElementByField(String fieldName, Object value) {
        checkFieldName(fieldName, value);
        Cursor cursor = executeSelectByFieldQuery(fieldName, value);
        cursor.moveToFirst();
        return objectFromCursor(cursor);
    }

    private List<T> cursorToObjectsList(Cursor cursor){
        int count = cursor.getCount();
        if(count <= 0){
            return new ArrayList<T>();
        }

        List<T> result = new ArrayList<T>(count);

        do {
            T object = objectFromCursor(cursor);
            result.add(object);
        } while (cursor.moveToNext());

        return result;
    }

    @Override
    public List<T> getElementsByField(String fieldName, Object value) {
        checkFieldName(fieldName, value);
        Cursor cursor = executeSelectByFieldQuery(fieldName, value);
        cursor.moveToFirst();
        return cursorToObjectsList(cursor);
    }

    @Override
    public void addOrReplace(T object) {
        ContentValues contentValues = getContentValues(object);
        database.replace(tableName, null, contentValues);
    }

    @Override
    public void remove(T object) {
        try {
            Object id = idField.get(object);
            removeObjectWithId(id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeObjectWithId(Object id) {
        String value = SqlUtilities.getSqlValue(id);
        String where = idColumnName + "=" + value;
        database.delete(tableName, where, null);
    }

    @Override
    public void removeObjectsByField(String fieldName, Object fieldValue) {
        String where;
        if (fieldValue != null) {
            String value = SqlUtilities.getSqlValue(fieldValue);
            where = fieldName + "=" + value;
        } else {
            where = fieldName + " is NULL";
        }
        database.delete(tableName, where, null);
    }

    @Override
    public List<T> getElements() {
        Cursor cursor = database.query(tableName, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursorToObjectsList(cursor);
    }

    @Override
    public void add(T object) {
        if(!SqlUtilities.isInt(idField.getType())){
            throw new UnsupportedOperationException("add is not supported for non-integer id, " +
                    "use addOrReplace instead");
        }

        ContentValues contentValues = getContentValues(object, idField);
        long id = database.insert(tableName, null, contentValues);
        try {
            if(idField.getType() == long.class){
                idField.setLong(object, id);
            } else if(idField.getType() == int.class) {
                if(id > Integer.MAX_VALUE){
                    throw new RuntimeException("id > Integer.MAX_VALUE");
                }

                idField.setInt(object, (int) id);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        database.delete(tableName, null, null);
    }

    @Override
    public Class getIdType() {
        return idField.getType();
    }

    @Override
    public Object getIdOf(T object) {
        try {
            return idField.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Class<T> getTableEntityClass() {
        return tClass;
    }

    protected abstract T createObject();
    protected abstract String getTableName();

    @Override
    public void close() {
        database.close();
    }
}
