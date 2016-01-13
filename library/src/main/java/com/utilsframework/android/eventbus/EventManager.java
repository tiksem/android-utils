package com.utilsframework.android.eventbus;

import com.utils.framework.Reflection;
import com.utils.framework.collections.map.MultiMap;
import com.utils.framework.collections.map.MultiMapEntry;
import com.utils.framework.collections.map.SetValuesHashMultiMap;
import com.utilsframework.android.eventbus.annotations.Subscribe;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by CM on 8/28/2015.
 */
public class EventManager {
    private static EventManager instance;
    private MultiMap<Class, EventListener> listeners = new SetValuesHashMultiMap<>();

    private class EventBusImpl implements EventBus {
        private MultiMap<Class, EventListener> registeredListeners = new SetValuesHashMultiMap<>();

        @Override
        public void destroy() {
            listeners.removeAll(registeredListeners);
            registeredListeners.clear();
        }

        @Override
        public void addEventListener(Class eventId, EventListener eventListener) {
            listeners.put(eventId, eventListener);
            registeredListeners.put(eventId, eventListener);
        }

        @Override
        public void removeEventListener(Class eventId, EventListener eventListener) {
            listeners.remove(eventId, eventListener);
            registeredListeners.remove(eventId, eventListener);
        }
    }

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }

        return instance;
    }

    public EventBus getBus(final Object subscriber) {
        EventBusImpl eventBus = new EventBusImpl();
        Method[] methods = subscriber.getClass().getMethods();
        for (final Method method : methods) {
            final Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if (subscribe != null) {
                eventBus.addEventListener(method.getParameterTypes()[0], new EventListener() {
                    @Override
                    public void onEvent(Object event) {
                        Reflection.executeMethod(subscriber, method, event);
                    }
                });
            }
        }

        return eventBus;
    }

    public void fire(Object event) {
        for (EventListener eventListener : listeners.getValues(event.getClass())) {
            eventListener.onEvent(event);
        }
    }
}
