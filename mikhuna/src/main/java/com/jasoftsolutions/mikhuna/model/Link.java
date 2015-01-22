package com.jasoftsolutions.mikhuna.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hugo on 21/01/2015.
 */
public class Link extends AbstractModel {

    private String link;

    @SerializedName("type_link")
    private Integer typeLink;

    private Long restaurantServerId;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getTypeLink() {
        return typeLink;
    }

    public void setTypeLink(Integer typeLink) {
        this.typeLink = typeLink;
    }

    public Long getRestaurantServerId() {
        return restaurantServerId;
    }

    public void setRestaurantServerId(Long restaurantServerId) {
        this.restaurantServerId = restaurantServerId;
    }
}
