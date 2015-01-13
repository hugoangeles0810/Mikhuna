package com.jasoftsolutions.mikhuna.remote.json;

import com.jasoftsolutions.mikhuna.model.RestaurantDishCategory;

import java.util.ArrayList;

/**
 * Created by Hugo on 12/01/2015.
 */
public class DishCategoriesResponseData {

    private Long lastUpdate;
    private Integer count;
    private ArrayList<RestaurantDishCategory> categories;

    public DishCategoriesResponseData() {
    }

    public DishCategoriesResponseData(Long lastUpdate, Integer count) {
        this.lastUpdate = lastUpdate;
        this.count = count;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<RestaurantDishCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<RestaurantDishCategory> categories) {
        this.categories = categories;
    }
}
