package com.jasoftsolutions.mikhuna.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pc07 on 14/10/2014.
 */
public class RestaurantUri {

    @SerializedName("__id__")
    private Integer id;

    @SerializedName("uri")
    private String uri;

    @SerializedName("url")
    private String fullUri;

    @SerializedName("id")
    private Integer restaurantServerId;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFullUri() {
        return fullUri;
    }
    public void setFullUri(String fullUri) {
        this.fullUri = fullUri;
    }

    public Integer getRestaurantServerId() {
        return restaurantServerId;
    }
    public void setRestaurantServerId(Integer restaurantServerId) {
        this.restaurantServerId = restaurantServerId;
    }
}
