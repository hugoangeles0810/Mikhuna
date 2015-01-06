package com.jasoftsolutions.mikhuna.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pc07 on 28/04/2014.
 */
public class RestaurantCategory extends AbstractModel {

    @SerializedName("ui")
    private Long parentUbigeoServerId;

    @SerializedName("n")
    private String name;

    public Long getParentUbigeoServerId() {
        return parentUbigeoServerId;
    }
    public void setParentUbigeoServerId(Long parentUbigeoServerId) {
        this.parentUbigeoServerId = parentUbigeoServerId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
