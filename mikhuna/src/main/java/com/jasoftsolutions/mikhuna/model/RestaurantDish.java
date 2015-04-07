package com.jasoftsolutions.mikhuna.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pc07 on 20/10/2014.
 */
public class RestaurantDish extends AbstractModel {

    private Long restaurantServerId;
    private Long dishCategoryServerId;
    private String name;
    private String description;
    private Integer position;
    private Double price;
    private Boolean liked;
    @SerializedName("cl")
    private Long likeCount;
    private List<RestaurantDishPresentation> dishPresentations;
    @SerializedName("s")
    private Integer state;

    @SerializedName("l")
    private Integer likedInt;

    private String priceText;

    public RestaurantDish() {}

    public RestaurantDish(String name) {
        this.name = name;
    }

    public RestaurantDish(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public RestaurantDish(String name, String description, String priceText, Boolean liked, Long likeCount) {
        this.name = name;
        this.description = description;
        this.priceText = priceText;
        this.liked = liked;
        this.likeCount = likeCount;
    }

    public RestaurantDish(Long restaurantServerId, Long dishCategoryServerId, String name, String description, Integer position, Double price, Boolean liked, Long likeCount) {
        this.restaurantServerId = restaurantServerId;
        this.dishCategoryServerId = dishCategoryServerId;
        this.name = name;
        this.description = description;
        this.position = position;
        this.price = price;
        this.liked = liked;
        this.likeCount = likeCount;
    }

    public Long getRestaurantServerId() {
        return restaurantServerId;
    }
    public void setRestaurantServerId(Long restaurantServerId) {
        this.restaurantServerId = restaurantServerId;
    }

    public Long getDishCategoryServerId() {
        return dishCategoryServerId;
    }
    public void setDishCategoryServerId(Long dishCategoryServerId) {
        this.dishCategoryServerId = dishCategoryServerId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPosition() {
        return position;
    }
    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getLikedIntegerValue(){ return liked==null?0:liked?1:0; }
    public Boolean getLiked() { return liked; }
    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Long getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public List<RestaurantDishPresentation> getDishPresentations() { return dishPresentations; }
    public void setDishPresentations(List<RestaurantDishPresentation> dishPresentations) {
        this.dishPresentations = dishPresentations;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getPriceText() {
        return priceText;
    }
    public void setPriceText(String priceText) {
        this.priceText = priceText;
    }

    public Integer getLikedInt() {
        return likedInt;
    }

    public void setLikedInt(Integer likedInt) {
        this.likedInt = likedInt;
    }
}
