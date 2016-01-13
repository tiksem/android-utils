package com.utilsframework.android.db;

import java.util.List;

/**
 * User: Tikhonenko.S
 * Date: 19.12.13
 * Time: 18:31
 */
public interface AsyncDataStore<T> {
    void getElementByField(String fieldName, Object value, OnResult<T> onResult);
    void getElementsByField(String fieldName, Object value, OnResult<List<T>> onResult);
    void getElements(OnResult<List<T>> onResult);
    void getElementById(Object id, OnResult<T> onResult);
    void addOrReplace(T object, OnFinish onFinish);
    void add(T object, OnFinish onFinish);
    void remove(T object, OnFinish onFinish);
    void removeObjectWithId(Object id, OnFinish onFinish);
    void removeObjectsByField(String fieldName, Object fieldValue, OnFinish onFinish);
    void clear(OnFinish onFinish);
}
