package com.jasoftsolutions.mikhuna.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pc07 on 28/04/2014.
 */
public class RestaurantCategoryAssign extends AbstractModel {

    @SerializedName("__local_restaurant")
    private Restaurant restaurant;

    @SerializedName("rci")
    private Long restaurantCategoryServerId;

    public Restaurant getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Long getRestaurantCategoryServerId() {
        return restaurantCategoryServerId;
    }
    public void setRestaurantCategoryServerId(Long restaurantCategoryServerId) {
        this.restaurantCategoryServerId = restaurantCategoryServerId;
    }
}
