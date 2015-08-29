package com.utilsframework.android.eventbus;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by CM on 8/28/2015.
 */
public class EventBusFragment extends Fragment {
    private EventBus eventBus;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        eventBus = EventManager.getInstance().getBus();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventBus.destroy();
    }

    protected void addEventListener(EventId eventId, EventListener eventListener) {
        eventBus.addEventListener(eventId, eventListener);
    }

    protected void removeEventListener(EventId eventId, EventListener eventListener) {
        eventBus.removeEventListener(eventId, eventListener);
    }
}
