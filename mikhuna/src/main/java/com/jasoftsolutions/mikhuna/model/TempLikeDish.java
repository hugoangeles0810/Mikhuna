package com.jasoftsolutions.mikhuna.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hugo on 26/02/2015.
 */
public class TempLikeDish{

    @SerializedName("id")
    private Long dishId;

    @SerializedName("f")
    private Integer likeState;

    public TempLikeDish() {
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Integer getLikeState() {
        return likeState;
    }

    public void setLikeState(Integer likeState) {
        this.likeState = likeState;
    }
}
