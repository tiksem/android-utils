package com.utilsframework.android.db;

import java.util.List;

/**
 * Created by Tikhonenko.S on 18.09.13.
 */
public interface DataStore<T> {
    T getElementByField(String fieldName, Object value);
    List<T> getElementsByField(String fieldName, Object value);
    List<T> getElements();
    T getElementById(Object id);
    void addOrReplace(T object);
    void add(T object);
    void remove(T object);
    void removeObjectWithId(Object id);
    void removeObjectsByField(String fieldName, Object fieldValue);
    Class getIdType();
    Object getIdOf(T object);
    void clear();
    void close();
    void changeFieldOfAllRecords(String fieldName, int diff);
    T getFirstElement();
}
