package com.jasoftsolutions.mikhuna.remote;

import android.content.Context;
import android.content.SharedPreferences;

import com.jasoftsolutions.mikhuna.BuildConfig;

/**
 * Created by pc07 on 03/04/2014.
 */
public class LastResourceUpdatePreferences {

    public static final String RESTAURANT_LIST = "restaurant_list";
    public static final String RESTAURANT_PROMOTIONS = "restaurant_promotions";
    public static final String MANAGEMENT = "management";
    public static final String VERSION = "version";

    private Context context;
    private SharedPreferences pref;

    public static SharedPreferences get(Context context) {
        return context.getSharedPreferences("last_resource_updates", Context.MODE_PRIVATE);
    }

    public LastResourceUpdatePreferences(Context context) {
        this.context = context;
        pref = get(context);
    }

    public SharedPreferences getPref() {
        return pref;
    }

    public long getManagementLastUpdate() {
        Integer version = pref.getInt(VERSION, 0);
        if (version < BuildConfig.VERSION_CODE){
            return 0;
        }

        return pref.getLong(MANAGEMENT, 0);
    }

    public void setManagementLastUpdate(long lastUpdate) {
        Integer version = pref.getInt(VERSION, 0);

        if (version == 0) pref.edit().putInt(VERSION, BuildConfig.VERSION_CODE).commit();
        pref.edit().putLong(MANAGEMENT, lastUpdate).commit();
    }

    public long getRestaurantListLastUpdate(long ubigeoId) {
        return pref.getLong(RESTAURANT_LIST + "_" + ubigeoId, 0);
    }

    public void setRestaurantListLastUpdate(long ubigeoId, long lastUpdate) {
        pref.edit().putLong(RESTAURANT_LIST + "_" + ubigeoId, lastUpdate).commit();
    }

    public long getRestaurantPromotionsLastUpdate(long ubigeoId) {
        return pref.getLong(RESTAURANT_PROMOTIONS + "_" + ubigeoId, 0);
    }

    public void setRestaurantPromotionsLastUpdate(long ubigeoId, long lastUpdate) {
        pref.edit().putLong(RESTAURANT_PROMOTIONS + "_" + ubigeoId, lastUpdate).commit();
    }
}
