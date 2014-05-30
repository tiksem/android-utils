package com.utilsframework.android.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tikhonenko.S
 * Date: 21.10.13
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */
public class SQLiteManyToMany<Id> implements ManyToMany<Id>{
    private String tableName;
    private String[] fields;
    private SQLiteDatabase database;
    private Class idClass;

    public SQLiteManyToMany(SQLiteDatabase database,
                            final String tableName,
                            Class idClass) {
        fields = new String[2];
        fields[0] = fields[1] = SqlUtilities.createIdType(idClass);
        SqlUtilities.createTableIfNotExists(fields, tableName, database);
        this.tableName = tableName;
        this.database = database;
        this.idClass = idClass;
    }

    @Override
    public boolean contains(Id aId, Id bId){
        String where = getWhere(aId, bId);
        Cursor cursor = database.query(tableName, fields, where, null, null, null, null);
        return cursor.getCount() > 0;
    }

    private String getWhere(Id aId, Id bId){
        return fields[0] + "=" + SqlUtilities.getSqlValue(aId) + " AND " +
                fields[1] + "=" + SqlUtilities.getSqlValue(bId);
    }

    private ContentValues getContentValue(Id source, Id destination){
        ContentValues contentValues = new ContentValues(2);
        SqlUtilities.putContentValue(contentValues, fields[0], source);
        SqlUtilities.putContentValue(contentValues, fields[1], destination);
        return contentValues;
    }

    @Override
    public void addTo(Id source, Id destination) {
        if(contains(source, destination)){
            return;
        }

        ContentValues contentValues = getContentValue(source, destination);
        database.insert(tableName, null, contentValues);
    }

    @Override
    public void addAllTo(List<Id> source, Id destination) {
        for(Id id : source){
            addTo(id, destination);
        }
    }

    @Override
    public void removeFrom(Id from, Id what) {
        String where = getWhere(from, what);
        database.delete(tableName, where, null);
    }

    @Override
    public void removeAllFrom(Id from, List<Id> what) {
        for(Id id : what){
            removeFrom(from, id);
        }
    }

    @Override
    public List<Id> getElementsFrom(Id from) {
        String where = fields[0] + "=" + SqlUtilities.getSqlValue(from);
        Cursor cursor = database.query(tableName, fields, where, null, null, null, null);
        int count = cursor.getCount();
        List<Id> result = new ArrayList<Id>(count);
        if(count <= 0){
            return result;
        }

        do {
            Id id = (Id)SqlUtilities.getColumnValueFromCursor(cursor, idClass, 1);
            result.add(id);
        } while (cursor.moveToNext());

        return result;
    }

    @Override
    public <Data> List<Data> getElementsFrom(Id from, DataStore<Data> dataStore) {
        List<Id> ides = getElementsFrom(from);
        List<Data> result = new ArrayList<Data>(ides.size());
        for(Id id : ides){
            Data data = dataStore.getElementById(id);
            result.add(data);
        }

        return result;
    }
}
