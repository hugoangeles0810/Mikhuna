package com.jasoftsolutions.mikhuna.remote.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc07 on 14/05/2014.
 */
public class ActionListenerManager {

    private List<ActionListener> listeners;

    public ActionListenerManager() {
        listeners = new ArrayList<ActionListener>();
    }

    public void addListener(ActionListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ActionListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void raiseActionPerformed(Object sender, int action) {
        for (ActionListener l : listeners) {
            if (l != null) {
                if (!l.actionPerformed(sender, action)) {
                    listeners.remove(l);
                }
            }
        }
    }
}
