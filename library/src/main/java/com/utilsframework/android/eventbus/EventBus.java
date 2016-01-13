package com.utilsframework.android.eventbus;

import com.utils.framework.Destroyable;

/**
 * Created by CM on 8/28/2015.
 */
public interface EventBus extends Destroyable {
    void addEventListener(Class eventId, EventListener eventListener);
    void removeEventListener(Class eventId, EventListener eventListener);
}
