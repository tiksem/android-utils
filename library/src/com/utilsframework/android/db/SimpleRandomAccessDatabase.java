package com.utilsframework.android.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.utils.framework.strings.Strings;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Администратор
 * Date: 05.07.13
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
public class SimpleRandomAccessDatabase<T> implements RandomAccessDataBase<T> {
    private SQLiteDatabase database;
    private String tableName;
    private Class<T> tClass;
    private int maxStringLength;
    private String[] columnNames;

    private void createTable(){
        ArrayList<String> columnNamesList = new ArrayList<String>();
        DatabaseUtilities.CreateTableForClassParams params = new DatabaseUtilities.CreateTableForClassParams();
        params.database = database;
        params.aClass = tClass;
        params.tableName = tableName;
        params.maxStringLength = maxStringLength;
        params.outColumnNames = columnNamesList;
        params.includeId = true;
        DatabaseUtilities.createTableForClass(params);

        columnNames = new String[columnNamesList.size()];
        columnNames = columnNamesList.toArray(columnNames);
    }

    public SimpleRandomAccessDatabase(String databaseName, String tableName, Class<T> tClass,
                                      int maxStringLength, Context context) {
        File file = context.getCacheDir();
        file = file.getAbsoluteFile();
        file = new File(file.toString() + '/' + databaseName);
        database = SQLiteDatabase.openOrCreateDatabase(file, null);
        this.tableName = tableName;
        this.tClass = tClass;
        this.maxStringLength = maxStringLength;

        createTable();
    }

    @Override
    public void set(int location, T object) {
        if(object == null){
            DatabaseUtilities.deleteObjectWithId(database, tableName, location);
            return;
        }

        List forJoin = new ArrayList();
        forJoin.add(DatabaseUtilities.ID_TABLE_NAME);
        forJoin.addAll(Arrays.asList(columnNames));
        String columns = Strings.joinObjects(",", forJoin).toString();

        List values = DatabaseUtilities.getObjectFieldValuesAsStringList(object);
        forJoin = new ArrayList();
        forJoin.add(location);
        forJoin.addAll(values);
        String valuesStatement = Strings.joinObjects(",", forJoin).toString();
        String sql = "insert or replace into " + tableName + " (" + columns + ") values(" + valuesStatement + ")";
        database.execSQL(sql);
    }

    @Override
    public T get(int i) {
        return DatabaseUtilities.selectObjectById(database, tableName, i, columnNames, tClass);
    }

    @Override
    public Map<Integer, T> toMap() {
        Cursor cursor = database.query(tableName, null, null, null, null, null, null);
        return DatabaseUtilities.cursorToMapWithIdKey(cursor, tClass);
    }
}
