package com.jasoftsolutions.mikhuna.activity.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.jasoftsolutions.mikhuna.data.ManagementManager;
import com.jasoftsolutions.mikhuna.domain.RestaurantListFilter;
import com.jasoftsolutions.mikhuna.domain.RestaurantServiceType;
import com.jasoftsolutions.mikhuna.util.ArrayUtil;

/**
* Created by pc07 on 05/05/2014.
*/
public class RestaurantListFilterPreferences {

    public static final String TAG = RestaurantListFilterPreferences.class.getSimpleName();

    public static final String PREF_NAME = "restaurant_list_filter";
    public static final String PREF_MAP = "map_list_filter";
    public static final String UBIGEO_SERVER_ID = "ubigeo_server_id";
    public static final String RESTAURANT_CATEGORIES = "restaurant_categories";
    public static final String SERVICES = "services";

    private SharedPreferences pref;

    public RestaurantListFilterPreferences(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public RestaurantListFilterPreferences(Context context, String prefName){
        pref= context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public void saveFilter(RestaurantListFilter filter) {
        if (filter == null) return;

        SharedPreferences.Editor editor = pref.edit();

        if (filter.getUbigeoServerId() == null) {
            editor.remove(UBIGEO_SERVER_ID);
        } else {
            editor.putLong(UBIGEO_SERVER_ID, filter.getUbigeoServerId());
        }

        if (filter.getRestaurantCategories() != null && filter.getRestaurantCategories().size() > 0) {
            editor.putString(RESTAURANT_CATEGORIES, ArrayUtil.implode(filter.getRestaurantCategories(), ","));
        } else {
            editor.remove(RESTAURANT_CATEGORIES);
        }

        if (filter.getServiceTypes() != null && filter.getServiceTypes().size() > 0) {
            editor.putString(SERVICES, ArrayUtil.implode(filter.getServiceTypes(), ","));
        } else {
            editor.remove(SERVICES);
        }

        editor.commit();
    }

    public RestaurantListFilter loadFilter() {
        RestaurantListFilter filter = new RestaurantListFilter();

        if (pref.contains(Generic.EXISTENT)) {
            if (pref.contains(UBIGEO_SERVER_ID)) {
                filter.setUbigeoServerId(pref.getLong(UBIGEO_SERVER_ID, 0));
            }

            if (pref.contains(RESTAURANT_CATEGORIES)) {
                filter.setRestaurantCategories(ArrayUtil.getArrayListOfLongFromString(
                        pref.getString(RESTAURANT_CATEGORIES, null), ","));
            }

            if (pref.contains(SERVICES)) {
                filter.setServiceTypes(ArrayUtil.getArrayListOfLongFromString(
                        pref.getString(SERVICES, null), ","));
            }
        } else {
            pref.edit().putBoolean(Generic.EXISTENT, true).commit();
            ManagementManager manager = new ManagementManager();

//            ArrayList<SelectOption> ubigeos = manager.getUbigeosFrom(1L);
//            if (ubigeos.size() > 0) {
//                filter.setUbigeoServerId(ubigeos.get(0).getId());
//            }

            filter.setRestaurantCategories(manager.getAllRestaurantCategoriesId());
            filter.setServiceTypes(RestaurantServiceType.getIdsArray());

            saveFilter(filter);
        }


        return filter;
    }

    public long getUbigeoId() {
        return pref.getLong(UBIGEO_SERVER_ID, 0);
    }
}
