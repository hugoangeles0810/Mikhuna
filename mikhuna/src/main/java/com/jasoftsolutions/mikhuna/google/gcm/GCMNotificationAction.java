package com.jasoftsolutions.mikhuna.google.gcm;

/**
 * Created by pc07 on 21/04/2014.
 */
public enum GCMNotificationAction {

    SIMPLE(0),
    RESTAURANT(1),
    PROMOTION(2);

    private int id;

    GCMNotificationAction(int id) {
        this.id = id;
    }

    public static GCMNotificationAction getById(int id) {
        for (GCMNotificationAction item : values()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
