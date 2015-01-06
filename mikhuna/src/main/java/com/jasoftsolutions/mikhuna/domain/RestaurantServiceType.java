package com.jasoftsolutions.mikhuna.domain;

import android.content.Context;

import com.jasoftsolutions.mikhuna.R;

import java.util.ArrayList;

/**
 * Created by pc07 on 15/04/2014.
 */
public enum RestaurantServiceType {

    DELIVERY(1, R.string.restaurant_service_type_delivery),
    BOOKING(2, R.string.restaurant_service_type_booking);
//    DELIVERY_AND_BOOKING(3, R.string.restaurant_service_type_delivery_and_booking),

    private int id;
    private int resourceId;

    RestaurantServiceType(int id, int resourceId) {
        this.id = id;
        this.resourceId = resourceId;
    }

    public static RestaurantServiceType getFromId(int id) {
        for (RestaurantServiceType rst : RestaurantServiceType.values()) {
            if (rst.id == id) return rst;
        }
        return null;
    }

    public static ArrayList<Long> getIdsArray() {
        ArrayList<Long> result = new ArrayList<Long>();
        for (RestaurantServiceType type : RestaurantServiceType.values()) {
            result.add((long)type.getId());
        }
        return result;
    }

    public static ArrayList<SelectOption> getSelectOptions(Context context) {
        ArrayList<SelectOption> data = new ArrayList<SelectOption>();
        for (RestaurantServiceType type : RestaurantServiceType.values()) {
            data.add(new SelectOption((long)type.getId(), context.getString(type.getResourceId())));
        }
        return data;
    }

    public int getId() {
        return id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public boolean match(int value) {
        return (value & id) == id;
    }
}
