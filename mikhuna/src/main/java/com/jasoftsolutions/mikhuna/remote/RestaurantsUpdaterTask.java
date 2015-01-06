package com.jasoftsolutions.mikhuna.remote;

import android.content.Context;

import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.remote.json.RestaurantListJsonResponse;
import com.jasoftsolutions.mikhuna.remote.listener.ActionListener;
import com.jasoftsolutions.mikhuna.remote.listener.ActionListenerManager;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc07 on 02/04/2014.
 */
public class RestaurantsUpdaterTask extends Thread {

    public static final int ACTION_COMPLETE = 0;

    private static final String THREAD_NAME = RestaurantsUpdaterTask.class.getName();
    private static final String TAG = RestaurantsUpdaterTask.class.getSimpleName();

    private Context context;

    private Boolean result;
    private RestaurantRemote rs;


//    private static RestaurantsUpdaterTask instance;

    private static Map<Context, RestaurantsUpdaterTask> instances = new HashMap<Context, RestaurantsUpdaterTask>();
    private static ActionListenerManager actionListenerManager = new ActionListenerManager();

    private RestaurantsUpdaterTask(Context context) {
        super(THREAD_NAME);
        this.context = context;
        rs = new RestaurantRemote();
    }

    public static RestaurantsUpdaterTask getInstance(Context context) {
        RestaurantsUpdaterTask instance = instances.get(context);
        if (instance == null || !instance.isAlive()) {
            instance = new RestaurantsUpdaterTask(context);
            instances.put(context, instance);
        }
        return instance;
    }

    public static void addActionListener(ActionListener listener) {
        actionListenerManager.addListener(listener);
    }

    public static void removeActionListener(ActionListener listener) {
        actionListenerManager.removeListener(listener);
    }

    public void startIfNecessary() {
        if (!isAlive()) start();
    }

    @Override
    public synchronized void run() {
        result = null;

        try {
            result = updateRestaurants();
        } catch (Exception e) {
            result = false;
            ExceptionUtil.handleException(e);
        }

        actionListenerManager.raiseActionPerformed(this, ACTION_COMPLETE);
        instances.remove(context);
    }

    private boolean updateRestaurants() {
        RestaurantListFilterPreferences filterPreferences = new RestaurantListFilterPreferences(context);
        LastResourceUpdatePreferences updatePreferences = new LastResourceUpdatePreferences(context);

        long ubigeoId = filterPreferences.getUbigeoId();
        long lastUpdate = updatePreferences.getRestaurantListLastUpdate(ubigeoId);

        RestaurantListJsonResponse restaurantList = rs.getRestaurantList(ubigeoId, lastUpdate);

        if (restaurantList!=null) {
            RestaurantManager rm=new RestaurantManager();
            rm.saveRestaurants(restaurantList.getResults());

            updatePreferences.setRestaurantListLastUpdate(ubigeoId, restaurantList.getLastUpdate());

            return true;
        } else {
            return false;
        }
    }

    public Boolean getResult() {
        return result;
    }

    public boolean isRunning() {
        return isAlive();
    }
}
