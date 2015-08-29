package com.utilsframework.android.eventbus;

import com.utils.framework.collections.map.MultiMap;
import com.utils.framework.collections.map.MultiMapEntry;
import com.utils.framework.collections.map.SetValuesHashMultiMap;

import java.util.*;

/**
 * Created by CM on 8/28/2015.
 */
public class EventManager {
    private static EventManager instance;
    private List<Set<EventListener>> listeners = new ArrayList<>();

    private class EventBusImpl implements EventBus {
        private MultiMap<EventId, EventListener> registeredListeners = new SetValuesHashMultiMap<>();

        @Override
        public void destroy() {
            Iterator<MultiMapEntry<EventId, EventListener>> iterator = registeredListeners.iterator();
            while (iterator.hasNext()) {
                MultiMapEntry<EventId, EventListener> next = iterator.next();
                listeners.get(next.key.id).remove(next.value);
            }
        }

        @Override
        public void addEventListener(EventId eventId, EventListener eventListener) {
            listeners.get(eventId.id).add(eventListener);
            registeredListeners.put(eventId, eventListener);
        }

        @Override
        public void removeEventListener(EventId eventId, EventListener eventListener) {
            listeners.get(eventId.id).remove(eventListener);
            registeredListeners.remove(eventId, eventListener);
        }
    }

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }

        return instance;
    }

    public EventBus getBus() {
        return new EventBusImpl();
    }

    public EventId nextId() {
        listeners.add(new HashSet<>());
        return new EventId();
    }

    public void fire(EventId eventId, Object data) {
        for (EventListener eventListener : listeners.get(eventId.id)) {
            eventListener.onEvent(eventId, data);
        }
    }

    public void fire(EventId eventId) {
        fire(eventId, null);
    }
}
