package com.jasoftsolutions.mikhuna.store;

import java.util.ArrayList;

/**
 * Created by pc07 on 26/08/2014.
 */
public abstract class AbstractStore {

    protected ArrayList<StoreListener> listeners;

    protected AbstractStore() {
        listeners = new ArrayList<StoreListener>();
    }

    public void addListener(StoreListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(StoreListener listener) {
        listeners.remove(listener);
    }

    protected void notifyOnReady(Object sender, Object data) {
        for (StoreListener l : listeners) {
            notifyOnReady(sender, data, l);
        }
    }

    protected void notifyOnReady(Object sender, Object data, StoreListener listener) {
        listener.onReady(sender, data);
        listener.onSuccess(sender, data);
    }

    protected void notifyOnUpdate(Object sender, Object data) {
        for (StoreListener l : listeners) {
            notifyOnUpdate(sender, data, l);
        }
    }

    protected void notifyOnUpdate(Object sender, Object data, StoreListener listener) {
        listener.onUpdate(sender, data);
        listener.onSuccess(sender, data);
    }

    protected void notifyOnTimeOut(Object sender, Object data) {
        for (StoreListener l : listeners) {
            notifyOnTimeOut(sender, data, l);
        }
    }

    protected void notifyOnTimeOut(Object sender, Object data, StoreListener listener) {
        listener.onTimeOut(sender, data);
        listener.onFail(sender, data);
    }

    protected void notifyOnFailedConnection(Object sender, Object data) {
        for (StoreListener l : listeners) {
            notifyOnFailedConnection(sender, data, l);
        }
    }

    protected void notifyOnFailedConnection(Object sender, Object data, StoreListener listener) {
        listener.onFailedConnection(sender, data);
        listener.onFail(sender, data);
    }
}
