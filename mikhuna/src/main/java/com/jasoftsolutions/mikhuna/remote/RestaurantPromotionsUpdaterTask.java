package com.jasoftsolutions.mikhuna.remote;

import android.content.Context;

import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.remote.json.RestaurantPromotionListJsonResponse;
import com.jasoftsolutions.mikhuna.remote.listener.ActionListener;
import com.jasoftsolutions.mikhuna.remote.listener.ActionListenerManager;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc07 on 02/04/2014.
 */
public class RestaurantPromotionsUpdaterTask extends Thread {

    public static final int ACTION_COMPLETE = 0;

    private static final String THREAD_NAME = RestaurantPromotionsUpdaterTask.class.getName();
    private static final String TAG = RestaurantPromotionsUpdaterTask.class.getSimpleName();

    private Context context;

    private Boolean result;
    private RestaurantRemote rs;


//    private static RestaurantsUpdaterTask instance;

    private static Map<Context, RestaurantPromotionsUpdaterTask> instances = new HashMap<Context, RestaurantPromotionsUpdaterTask>();
    private static ActionListenerManager actionListenerManager = new ActionListenerManager();

    private RestaurantPromotionsUpdaterTask(Context context) {
        super(THREAD_NAME);
        this.context = context;
        rs = new RestaurantRemote();
    }

    public static RestaurantPromotionsUpdaterTask getInstance(Context context) {
        RestaurantPromotionsUpdaterTask instance = instances.get(context);
        if (instance == null || !instance.isAlive()) {
            instance = new RestaurantPromotionsUpdaterTask(context);
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
            result = updateRestaurantPromotions();
        } catch (Exception e) {
            result = false;
            ExceptionUtil.handleException(e);
        }

        actionListenerManager.raiseActionPerformed(this, ACTION_COMPLETE);
        instances.remove(context);
    }

    private boolean updateRestaurantPromotions() {
        RestaurantListFilterPreferences filterPreferences = new RestaurantListFilterPreferences(context);
        LastResourceUpdatePreferences updatePreferences = new LastResourceUpdatePreferences(context);

        long ubigeoId = filterPreferences.getUbigeoId();
        long lastUpdate = updatePreferences.getRestaurantPromotionsLastUpdate(ubigeoId);

        RestaurantPromotionListJsonResponse restaurantPromotionList =
                rs.getRestaurantPromotionList(ubigeoId, lastUpdate);

        if (restaurantPromotionList!=null) {
            RestaurantManager rm=new RestaurantManager();
            rm.saveRestaurantPromotions(restaurantPromotionList.getResults());

            updatePreferences.setRestaurantPromotionsLastUpdate(ubigeoId, restaurantPromotionList.getLastUpdate());

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
