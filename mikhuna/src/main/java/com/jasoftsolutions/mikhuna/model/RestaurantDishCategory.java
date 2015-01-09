package com.jasoftsolutions.mikhuna.model;

import java.util.List;

/**
 * Created by pc07 on 20/10/2014.
 */
public class RestaurantDishCategory extends AbstractModel {

    private Long restaurantServerId;
    private String name;
    private Integer position;
    private Long dishesLastUpdate;
    private List<RestaurantDish> restaurantDishes;

    public RestaurantDishCategory() {}

    public RestaurantDishCategory(String name) {
        this.name = name;
    }

    public RestaurantDishCategory(Long serverId, Long restaurantServerId, String name) {
        this.setServerId(serverId);
        this.restaurantServerId = restaurantServerId;
        this.name = name;
    }

    public RestaurantDishCategory(Long restaurantServerId, String name, Integer position, Long dishesLastUpdate) {
        this.restaurantServerId = restaurantServerId;
        this.name = name;
        this.position = position;
        this.dishesLastUpdate = dishesLastUpdate;
    }

    public Long getRestaurantServerId() {
        return restaurantServerId;
    }
    public void setRestaurantServerId(Long restaurantServerId) {
        this.restaurantServerId = restaurantServerId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }
    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getDishesLastUpdate() {
        return dishesLastUpdate;
    }
    public void setDishesLastUpdate(Long dishesLastUpdate) {
        this.dishesLastUpdate = dishesLastUpdate;
    }

    public List<RestaurantDish> getRestaurantDishes() { return restaurantDishes; }
    public void setRestaurantDishes(List<RestaurantDish> restaurantDishes) {
        this.restaurantDishes = restaurantDishes;
    }

    @Override
    public String toString() {
        return name;
    }
}
