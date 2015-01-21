package com.jasoftsolutions.mikhuna.util;

import android.content.Context;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

/**
 * Created by Hugo on 21/01/2015.
 */
public class AnalyticsUtil {

    public static void registerView(Context context, String name){
        Tracker tracker = EasyTracker.getInstance(context);
        tracker.set(Fields.SCREEN_NAME, name);
        tracker.send(MapBuilder.createAppView().build());
    }

    public static void registerEvent(Context context, String category, String action, String label, Long value){
        Tracker tracker = EasyTracker.getInstance(context);
        tracker.send(MapBuilder.createEvent(category, action, label, value).build());
    }

    public static void registerEvent(Context context, String category, String action, String label){
        registerEvent(context, category, action, label, null);
    }

    public static void registerEvent(Context context, String category, String action){
        registerEvent(context, category, action, null, null);
    }
}
