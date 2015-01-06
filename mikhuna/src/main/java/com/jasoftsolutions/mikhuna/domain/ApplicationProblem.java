package com.jasoftsolutions.mikhuna.domain;

/**
 * Created by pc07 on 06/05/2014.
 */
public class ApplicationProblem {

    private long restaurantServerId;

    private int problemTypeId;

    private String detail;

    private String user;

    public long getRestaurantServerId() {
        return restaurantServerId;
    }

    public void setRestaurantServerId(long restaurantServerId) {
        this.restaurantServerId = restaurantServerId;
    }

    public int getProblemTypeId() {
        return problemTypeId;
    }

    public void setProblemTypeId(int problemTypeId) {
        this.problemTypeId = problemTypeId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
