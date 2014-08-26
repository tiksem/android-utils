package com.utilsframework.android.db;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Администратор
 * Date: 05.07.13
 * Time: 19:51
 * To change this template use File | Settings | File Templates.
 */
public interface RandomAccessDataBase<T> {
    T get(int index);
    void set(int index, T value);
    Map<Integer,T> toMap();
}
