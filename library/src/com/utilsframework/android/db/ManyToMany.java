package com.utilsframework.android.db;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Tikhonenko.S
 * Date: 21.10.13
 * Time: 17:41
 * To change this template use File | Settings | File Templates.
 */
public interface ManyToMany<Id> {
    void addTo(Id source, Id destination);
    void addAllTo(List<Id> source, Id destination);
    void removeFrom(Id from, Id what);
    void removeAllFrom(Id from, List<Id> what);
    List<Id> getElementsFrom(Id from);
    <Data> List<Data> getElementsFrom(Id from, DataStore<Data> dataStore);
    boolean contains(Id source, Id what);
}
