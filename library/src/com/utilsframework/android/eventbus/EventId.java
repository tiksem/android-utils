package com.utilsframework.android.eventbus;

/**
 * Created by CM on 8/28/2015.
 */
public class EventId {
    static int count = 0;
    int id;

    EventId() {
        id = count++;
    }
}
