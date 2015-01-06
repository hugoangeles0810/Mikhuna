package com.jasoftsolutions.mikhuna.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantTimetable extends AbstractModel {

    private Restaurant restaurant;

    @SerializedName("wd")
    private Integer weekday;

    @SerializedName("st")
    private String startTime;

    @SerializedName("ft")
    private String finishTime;

    public Restaurant getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Integer getWeekday() {
        return weekday;
    }
    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }
    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
}
