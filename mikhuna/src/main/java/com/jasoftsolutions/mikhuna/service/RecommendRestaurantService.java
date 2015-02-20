package com.jasoftsolutions.mikhuna.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jasoftsolutions.mikhuna.activity.RestaurantListActivity;
import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.domain.RestaurantListFilter;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.remote.LastResourceUpdatePreferences;
import com.jasoftsolutions.mikhuna.remote.RestaurantRemote;
import com.jasoftsolutions.mikhuna.util.AccountUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

import java.util.ArrayList;

/**
 * Created by Hugo on 19/02/2015.
 */
public class RecommendRestaurantService extends IntentService {

    public static final String TAG = RecommendRestaurantService.class.getSimpleName();
    public static final String SERVICE_NAME = RecommendRestaurantService.class.getSimpleName();
    public static final String ACTION_RECOMMENDED = "action_recommended";

    public RecommendRestaurantService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent.getAction().equals(ACTION_RECOMMENDED)) {

            RestaurantRemote rr = new RestaurantRemote();
            RestaurantListFilterPreferences rfp = new RestaurantListFilterPreferences(this);
            Long ubigeoId = rfp.getUbigeoId();
            String user = AccountUtil.getDefaultGoogleAccount(this);

            Long restaurantID = null;

            try {
                restaurantID = rr.getIdOfRestaurantRecommended(ubigeoId, user);
            } catch (Exception e) {
                ExceptionUtil.ignoreException(e);
            }

            if (restaurantID == null) {
                RestaurantManager rm = new RestaurantManager();
                ArrayList<Long> restaurantsId = rm.queryRestaurantServerIds(null, rfp.loadFilter());
                int index = (int) (Math.random() * restaurantsId.size());
                restaurantID = restaurantsId.get(index);
            }


            Intent i = new Intent(RestaurantListActivity.ACTION_SHOW_RECOMMENDED);
            i.putExtra(RestaurantListActivity.RESTAURANT_ID, restaurantID);

            sendBroadcast(i);

        }
    }

    public static Intent getIntentOfActionRecommended(Context context){
        Intent intent = new Intent(context, RecommendRestaurantService.class);
        intent.setAction(ACTION_RECOMMENDED);
        return intent;
    }

}
