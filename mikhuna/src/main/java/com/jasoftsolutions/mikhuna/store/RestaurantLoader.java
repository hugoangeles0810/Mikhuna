package com.jasoftsolutions.mikhuna.store;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.remote.RestaurantRemote;

/**
 * Created by Hugo on 04/02/2015.
 */
public class RestaurantLoader extends AsyncTaskLoader<Restaurant>{

    private static final String TAG = RestaurantLoader.class.getSimpleName();

    private Restaurant mRestaurant;
    private long mServerId;

    public RestaurantLoader(Context context, long serverId) {
        super(context);
        mServerId = serverId;
    }

    @Override
    public Restaurant loadInBackground() {
        RestaurantManager rm = new RestaurantManager();
        Restaurant restaurant = rm.getRestaurantByServerId(mServerId);

        if (restaurant == null){
            try {
                RestaurantRemote rr = new RestaurantRemote();
                restaurant = rr.getRestaurant(mServerId);
                if (restaurant != null){
                    rm.saveRestaurant(restaurant);
                }
            }catch (Exception e){
                restaurant = null;
            }
        }

        mRestaurant = restaurant;

        return mRestaurant;
    }

    @Override
    protected void onStartLoading() {
        if (mRestaurant != null){
            deliverResult(mRestaurant);
        }

        if (takeContentChanged() || mRestaurant == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        if (mRestaurant != null){
            mRestaurant = null;
        }
    }

}