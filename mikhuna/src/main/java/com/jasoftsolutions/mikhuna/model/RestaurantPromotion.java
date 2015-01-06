package com.jasoftsolutions.mikhuna.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pc07 on 28/04/2014.
 */
public class RestaurantPromotion extends AbstractModel {

    @SerializedName("__local_restaurant")
    private Restaurant restaurant;

    @SerializedName("ri")
    private Long restaurantServerId;

    @SerializedName("n")
    private String title;

    @SerializedName("sd")
    private Long startDate;

    @SerializedName("ed")
    private Long finishDate;

    @SerializedName("d")
    private String description;

    @SerializedName("w")
    private Integer weight;

    public Restaurant getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Long getStartDate() {
        return startDate;
    }
    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getFinishDate() {
        return finishDate;
    }
    public void setFinishDate(Long finishDate) {
        this.finishDate = finishDate;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRestaurantServerId() {
        return restaurantServerId;
    }
    public void setRestaurantServerId(Long restaurantServerId) {
        this.restaurantServerId = restaurantServerId;
    }

    public Integer getWeight() {
        return weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
