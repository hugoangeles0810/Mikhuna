package com.jasoftsolutions.mikhuna.remote;

import android.content.Context;

import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.model.Restaurant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc07 on 04/04/2014.
 */
public class RestaurantDetailUpdaterTask extends Thread {

    private static final String THREAD_NAME = RestaurantDetailUpdaterTask.class.getName();
    private static final String TAG = RestaurantDetailUpdaterTask.class.getSimpleName();

    private Context context;
    private Long restaurantId;

    private Object objectToNotify;

    private static Map<Long, RestaurantDetailUpdaterTask> instances=new HashMap<Long, RestaurantDetailUpdaterTask>();

    private RestaurantDetailUpdaterTask(Context context, Long restaurantId) {
        super(THREAD_NAME);
        this.context = context;
        this.restaurantId = restaurantId;
    }

    public static RestaurantDetailUpdaterTask getInstance(Context context, Long restaurantId) {
        RestaurantDetailUpdaterTask instance=instances.get(restaurantId);
        if (instance==null || !instance.isAlive()) {
            instance = new RestaurantDetailUpdaterTask(context, restaurantId);
            instances.put(restaurantId, instance);
        }
        return instance;
    }

    public void startIfNecessary() {
        if (!isAlive()) start();
    }

    public Object getObjectToNotify() {
        return objectToNotify;
    }

    public void setObjectToNotify(Object objectToNotify) {
        this.objectToNotify = objectToNotify;
    }

    @Override
    public void run() {
        RestaurantRemote rs=new RestaurantRemote();

        Long lastUpdate=null;
        RestaurantManager rm=new RestaurantManager();
        Restaurant r=rm.getRestaurantById(restaurantId);
        if (r != null) {
            lastUpdate = r.getLastUpdate();
        }

        Restaurant restaurant = rs.getRestaurant(r.getServerId(), lastUpdate);

        if (restaurant!=null && restaurant.getLastUpdate()>lastUpdate) {
            rm.saveRestaurant(restaurant);
        }

        if (objectToNotify!=null) {
            synchronized (objectToNotify) {
                objectToNotify.notifyAll();
            }
        }

        instances.remove(restaurantId);
    }
}
