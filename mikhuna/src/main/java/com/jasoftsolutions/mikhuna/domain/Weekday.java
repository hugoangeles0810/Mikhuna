package com.jasoftsolutions.mikhuna.domain;

import com.jasoftsolutions.mikhuna.R;

/**
 * Created by pc07 on 09/04/2014.
 */
public enum Weekday {
    MONDAY(1, R.string.weekday_monday),
    TUESDAY(2, R.string.weekday_tuesday),
    WEDNESDAY(3, R.string.weekday_wednesday),
    THURSDAY(4, R.string.weekday_thursday),
    FRIDAY(5, R.string.weekday_friday),
    SATURDAY(6, R.string.weekday_saturday),
    SUNDAY(7, R.string.weekday_sunday);

    private int id;
    private int resourceId;

    Weekday(int id, int resourceId) {
        this.id = id;
        this.resourceId = resourceId;
    }

    public int getId() {
        return id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public static Weekday getFromId(int id) {
        for (Weekday w : Weekday.values()) {
            if (w.id == id) return w;
        }
        return null;
    }

}
