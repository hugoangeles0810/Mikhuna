package com.jasoftsolutions.mikhuna.model;

/**
 * Created by pc07 on 08/04/2014.
 */
public class Audit {

    private Long id;
    private Integer actionId;
    private Long actionDate;
    private Long restaurantServerId;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActionId() {
        return actionId;
    }
    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public Long getActionDate() {
        return actionDate;
    }
    public void setActionDate(Long actionDate) {
        this.actionDate = actionDate;
    }

    public Long getRestaurantServerId() {
        return restaurantServerId;
    }
    public void setRestaurantServerId(Long restaurantServerId) {
        this.restaurantServerId = restaurantServerId;
    }
}
