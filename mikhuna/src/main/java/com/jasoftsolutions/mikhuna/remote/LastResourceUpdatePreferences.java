package com.jasoftsolutions.mikhuna.remote;

import android.content.Context;
import android.content.SharedPreferences;

import com.jasoftsolutions.mikhuna.BuildConfig;
import com.jasoftsolutions.mikhuna.model.Version;

/**
 * Created by pc07 on 03/04/2014.
 */
public class LastResourceUpdatePreferences {

    public static final String RESTAURANT_LIST = "restaurant_list";
    public static final String RESTAURANT_PROMOTIONS = "restaurant_promotions";
    public static final String MANAGEMENT = "management";
    public static final String VERSION = "version";
    public static final String VERSION_STATE = "version_state";
    public static final String VERSION_URL = "version_url";
    public static final String VERSION_MSG = "version_msg";

    private Context context;
    private SharedPreferences pref;

    public LastResourceUpdatePreferences(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("last_resource_updates", Context.MODE_PRIVATE);;
    }

    public SharedPreferences getPref() {
        return pref;
    }

    public  long getManagementLastUpdate() {
        int version = pref.getInt(VERSION, 0);
        int build = BuildConfig.VERSION_CODE;
        long lastUpdate;
        if (version < build){
            pref.edit().clear()
                    .putInt(VERSION, build).apply();
        }
        lastUpdate = pref.getLong(MANAGEMENT, 0);
        return lastUpdate;
    }

    public void setManagementLastUpdate(long lastUpdate) {
        pref.edit().putLong(MANAGEMENT, lastUpdate).apply();
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

    public void setVersion(Version version){
        if (version != null){
            if (version.getState() != null){
                pref.edit().putInt(VERSION_STATE, version.getState())
                .putString(VERSION_URL, version.getUrl())
                .putString(VERSION_MSG, version.getMsg()).apply();
            }
        }
    }

    public Version getVersion(){
        int state = pref.getInt(VERSION_STATE, Version.VERSION_OK);
        String url = pref.getString(VERSION_URL, null);
        String msg = pref.getString(VERSION_MSG, null);
        return new Version(state, url, msg);
    }
}
