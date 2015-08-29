package com.utilsframework.android.eventbus;

import com.utils.framework.Destroyable;

/**
 * Created by CM on 8/28/2015.
 */
public interface EventBus extends Destroyable {
    void addEventListener(EventId eventId, EventListener eventListener);
    void removeEventListener(EventId eventId, EventListener eventListener);
}
