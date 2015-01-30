package com.jasoftsolutions.mikhuna.store;

import android.os.AsyncTask;
import android.util.Log;

import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.RestaurantDish;
import com.jasoftsolutions.mikhuna.model.RestaurantDishCategory;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.remote.RestaurantRemote;
import com.jasoftsolutions.mikhuna.remote.json.DishCategoriesResponseData;
import com.jasoftsolutions.mikhuna.util.ContextUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.ThreadUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by pc07 on 26/08/2014.
 */
public class RestaurantStore extends AbstractStore {

    private static final String TAG = RestaurantStore.class.getSimpleName();

    private static RestaurantStore instance;

    private boolean remoteRequesting;

    private RestaurantStore() {
        remoteRequesting = false;
    }

    public void requestRestaurantByLocalId(StoreListener listener, long localId) {
        requestRestaurantByLocalId(listener, localId, false);
    }

    public void requestRestaurantByLocalId(StoreListener listener, long localId, boolean offline) {
        RestaurantManager rm = new RestaurantManager();
        Restaurant r = rm.getRestaurantById(localId);
        if (r != null) {
            requestRestaurant(listener, r.getServerId(), offline);
        }
    }

    public void offlineRequestRestaurant(final StoreListener listener, final long serverId) {
        Thread localRequest = new Thread(new Runnable() {
            @Override
            public void run() {
                RestaurantManager rm = new RestaurantManager();
                Restaurant r = rm.getRestaurantWithFullDataByServerId(serverId);
                if (r != null) {
                    notifyOnReady(RestaurantStore.this, r, listener);
//                    if (r.getLastUpdate() > 0) {
//                    } else {
//                        notifyOnFailedConnection(RestaurantStore.this, r, listener);
//                    }
                }
            }
        });
        localRequest.start();
    }

    public void requestAllRestaurants(final StoreListener listener){
        Thread request = new Thread(new Runnable() {
            @Override
            public void run() {
                final AsyncTask<Void, Void, ArrayList<Restaurant>> task = new AsyncTask<Void, Void, ArrayList<Restaurant>>() {
                    @Override
                    protected ArrayList<Restaurant> doInBackground(Void... params) {
                        RestaurantRemote rr = new RestaurantRemote();
                        ArrayList<Restaurant> restaurants =  rr.getAllRestaurants();
                        return restaurants;
                    }
                };
                ArrayList<Restaurant> restaurants;
                task.execute();

                try {
                    restaurants = task.get(Const.MAX_UPDATING_MILLISECONDS_DELAY, TimeUnit.MILLISECONDS);
                    notifyOnReady(RestaurantStore.this, restaurants, listener);
                } catch (Exception e) {
                    task.cancel(true);
                    notifyOnTimeOut(RestaurantStore.this, null, listener);
                }
            }
        });
        request.start();
    }

    public void requestRestaurant(StoreListener listener, final long serverId) {
        requestRestaurant(listener, serverId, false);
    }

    public void requestRestaurant(StoreListener listener, final long serverId, boolean offline) {
        if (offline) {
            offlineRequestRestaurant(listener, serverId);
        } else {
            requestRestaurant(listener, serverId, Const.MAX_UPDATING_MILLISECONDS_DELAY);
        }
    }

    public void requestRestaurant(final StoreListener listener, final long serverId,
                                  final int maxTimeDelay) {
        Thread localRequest = new Thread(new Runnable() {
            @Override
            public void run() {
                final RestaurantManager rm = new RestaurantManager();
                final Restaurant r = rm.getRestaurantWithFullDataByServerId(serverId);

                final Map<String, Boolean> status = new HashMap<String, Boolean>();
                status.put("failed", false);
                status.put("ready", false);

                if (r != null && r.getLastUpdate() > 0) {
                    notifyOnReady(RestaurantStore.this, r, listener);
                    status.put("ready", true);
                }

                if (!remoteRequesting) {
                    remoteRequesting = true;
                    Thread remoteRequest = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Long lastUpdate = 0l;
                                if (r != null) {
                                    lastUpdate = r.getLastUpdate();
                                }
                                RestaurantRemote rr = new RestaurantRemote();

                                Restaurant retrieved = rr.getRestaurant(serverId, lastUpdate);
                                if (retrieved == null) {
                                    status.put("failed", true);
                                    if (!status.get("ready")) {
                                        notifyOnFailedConnection(RestaurantStore.this, r, listener);
                                    }
                                }
                                else if (retrieved.getLastUpdate() > lastUpdate) {
                                    rm.saveRestaurant(retrieved);
                                    // recargar desde el manager para que calcule todos los datos necesarios
                                    retrieved = rm.getRestaurantWithFullDataByServerId(retrieved.getServerId());
                                    notifyOnUpdate(RestaurantStore.this, retrieved);
                                }
                            } finally {
                                remoteRequesting = false;
                            }
                        }
                    });
                    try {
                        remoteRequest.start();
                        remoteRequest.join(maxTimeDelay);
                        if (remoteRequesting && !status.get("failed")) {
                            notifyOnTimeOut(RestaurantStore.this, r, listener);
                        }
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }
                }
            }
        });
        localRequest.start();
    }

    public void requestRestaurantServerIdFromUri(final StoreListener listener, final String uri) {
        Thread localRequest = new Thread(new Runnable() {
            @Override
            public void run() {
                final RestaurantManager rm = new RestaurantManager();
                final Long serverId = rm.getRestaurantServerIdByUri(uri);

                final Map<String, Boolean> status = new HashMap<String, Boolean>();
                status.put("failed", false);
                status.put("ready", false);

                if (serverId != null) {
                    notifyOnReady(RestaurantStore.this, serverId, listener);
                    status.put("ready", true);
                } else {
                    Thread remoteRequest = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RestaurantRemote rr = new RestaurantRemote();
                                Long sid = rr.getRestaurantServerIdFromUri(uri);

                                if (sid == null) {
                                    status.put("failed", true);
                                    if (!status.get("ready")) {
                                        notifyOnFailedConnection(RestaurantStore.this, sid, listener);
                                    }
                                }
                                else {
                                    rm.setRestaurantUri(sid, uri, null);
                                    notifyOnUpdate(RestaurantStore.this, sid, listener);
                                }
                            } catch (Exception e){
                                ExceptionUtil.handleException(e);
                            }
                        }
                    });
                    remoteRequest.start();
                }
            }
        });
        localRequest.start();
    }

    public void requestRestaurantFullUrlFromServerId(final StoreListener listener, final Long serverId) {
        Thread localRequest = new Thread(new Runnable() {
            @Override
            public void run() {
                final RestaurantManager rm = new RestaurantManager();
//                final Long serverId = rm.getRestaurantServerIdByUri(uri);
                final String fullUrl = rm.getLastRestaurantFullUrlFromServerId(serverId);

                final Map<String, Boolean> status = new HashMap<String, Boolean>();
                status.put("failed", false);
                status.put("ready", false);

                if (fullUrl != null) {
                    notifyOnReady(RestaurantStore.this, fullUrl, listener);
                    status.put("ready", true);
                } else {
                    Thread remoteRequest = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RestaurantRemote rr = new RestaurantRemote();
                                String retrivedUrl = rr.getRestaurantFullUrlFromServerId(serverId);

                                if (retrivedUrl == null) {
                                    status.put("failed", true);
                                    if (!status.get("ready")) {
                                        notifyOnFailedConnection(RestaurantStore.this, retrivedUrl, listener);
                                    }
                                }
                                else {
                                    rm.setRestaurantUri(serverId, null, retrivedUrl);
                                    notifyOnUpdate(RestaurantStore.this, retrivedUrl, listener);
                                }
                            } catch (Exception e){
                                ExceptionUtil.handleException(e);
                            }
                        }
                    });
                    remoteRequest.start();
                }
            }
        });
        localRequest.start();
    }

    public void requestRestaurantDishCategoriesOf(final Long restaurantServerId , final StoreListener listener) {
        Thread request = new Thread(new Runnable() {
            @Override
            public void run() {
                final RestaurantRemote rr = new RestaurantRemote();
                final RestaurantManager rm = new RestaurantManager();
                final ArrayList<RestaurantDishCategory> rdc;
                DishCategoriesResponseData responseData;
                final Map<String, Boolean> status = new HashMap<String, Boolean>();
                status.put("failed", false);
                status.put("ready", false);

                final Restaurant r = rm.getRestaurantByServerId(restaurantServerId);

                try {
                    responseData = rr.getRestaurantDishCategoryList(restaurantServerId, r.getCategoryLastUpdate());
                }catch (Exception e){
                    ExceptionUtil.handleException(e);
                    status.put("failed", true);
                    responseData = null;
                }

                if (!status.get("failed") && responseData.getCategories()!=null){
                    rm.saveRestaurantDishCategories(responseData.getCategories());
                    rm.updateRestaurantCategoryLastUpdate(restaurantServerId, responseData.getLastUpdate());
                }

                rdc = rm.getRestaurantDishCategoriesOf(restaurantServerId);

                if (status.get("failed")){
                    notifyOnFailedConnection(RestaurantStore.this, rdc, listener);
                }else{
                    notifyOnReady(RestaurantStore.this, rdc, listener);
                }

            }
        });
        request.start();
//        Thread localRequest = new Thread(new Runnable() {
//            @Override
//            public void run() {
//            final RestaurantManager rm = new RestaurantManager();
//            final Map<String, Boolean> status = new HashMap<String, Boolean>();
//            status.put("failed", false);
//            status.put("ready", false);
//            final ArrayList<RestaurantDishCategory> dishCategories =
//                    rm.getRestaurantDishCategoriesOf(restaurantServerId);
//            if (dishCategories!=null && !dishCategories.isEmpty()){
//                notifyOnReady(RestaurantStore.this, dishCategories, listener);
//                status.put("ready", true);
//            }else{
//                Thread remoteRequest = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            RestaurantRemote rr = new RestaurantRemote();
//                            ArrayList<RestaurantDishCategory> rdc;
//                            rdc = rr.getRestaurantDishCategoryList(restaurantServerId, lastUpdate).getCategories();
//
//                            if (rdc == null || rdc.isEmpty()){
//                                status.put("failed", true);
//                                if (!status.get("ready")){
//                                    notifyOnFailedConnection(RestaurantStore.this, rdc, listener);
//                                }
//                            }else{
//                                rm.saveRestaurantDishCategories(rdc);
//                                rdc = rm.getRestaurantDishCategoriesOf(restaurantServerId);
//                                notifyOnReady(RestaurantStore.this, rdc, listener);
//                            }
//                        }catch (Exception e){
//                            ExceptionUtil.handleException(e);
//                        }
//                    }
//                });
//                remoteRequest.start();
//            }
//
//            }
//        });
//        localRequest.start();
    }

    public void requestRestaurantDishesOf(final Long dishCategoryServerId, final StoreListener listener) {
        Thread localRequest = new Thread(new Runnable() {
            @Override
            public void run() {
                final RestaurantManager rm = new RestaurantManager();
                ArrayList<RestaurantDish> dishes =
                        rm.getRestaurantDishesOf(dishCategoryServerId);

                notifyOnReady(RestaurantStore.this, dishes, listener);
            }
        });
        localRequest.start();
    }

//    public void likeRestaurantDish(final Long dishServerId, final StoreListener listener){
//        Thread localRequest = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final RestaurantManager rm = new RestaurantManager();
//                rm.likeRestaurantDish(dishServerId);
//
//                notifyOnReady(RestaurantStore.this, null, listener);
//            }
//        });
//        localRequest.start();
//    }
//
//    public void unlikeRestaurantDish(final Long dishServerId, final StoreListener listener){
//        Thread localRequest = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final RestaurantManager rm = new RestaurantManager();
//                rm.unlikeRestaurantDish(dishServerId);
//
//                notifyOnReady(RestaurantStore.this, null, listener);
//            }
//        });
//        localRequest.start();
//    }

    public void toggleLikeRestaurantDish(final RestaurantDish restaurantDish, final StoreListener listener) {
        Thread localRequest = new Thread(new Runnable() {
            @Override
            public void run() {
                final RestaurantManager rm = new RestaurantManager();

//                if (rm.restaurantDishLiked(restaurantDish.getServerId())) {
//                    rm.likeRestaurantDish(restaurantDish);
//                } else {
//                    rm.unlikeRestaurantDish(restaurantDish);
//                }

                long likeCount = 0;
                if (restaurantDish.getLikeCount() != null) {
                    likeCount = restaurantDish.getLikeCount();
                }
                if (restaurantDish.getLiked() != null && restaurantDish.getLiked()) {
                    restaurantDish.setLiked(false);
                    if (likeCount > 0) likeCount--;
                } else {
                    restaurantDish.setLiked(true);
                    likeCount++;
                }
                restaurantDish.setLikeCount(likeCount);

                // TODO: Envío de la petición al servidor

                notifyOnReady(RestaurantStore.this, null, listener);
            }
        });
        localRequest.start();
    }

    public static RestaurantStore getInstance() {
        if (instance == null) {
            instance = new RestaurantStore();
        }
        return instance;
    }
}
