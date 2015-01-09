package com.jasoftsolutions.mikhuna.model;

/**
 * Created by Hugo on 08/01/2015.
 */
public class RestaurantDishPresentation extends AbstractModel {

    private Long restaurantDishServerId;
    private Long dishPresentationServerId;
    private String name;
    private Integer position;
    private Double cost;
    private String costText;

    public RestaurantDishPresentation(){}

    public RestaurantDishPresentation(String name) { this.name = name; }

    public RestaurantDishPresentation(String name, String costText) {
        this.name = name;
        this.costText = costText;
    }

    public RestaurantDishPresentation(String name, Integer position, String costText) {
        this.name = name;
        this.position = position;
        this.costText = costText;
    }

    public RestaurantDishPresentation(Long restaurantServerId, Long dishPresentationServerId, String name, Integer position, Double cost) {
        this.restaurantDishServerId = restaurantServerId;
        this.dishPresentationServerId = dishPresentationServerId;
        this.name = name;
        this.position = position;
        this.cost = cost;
    }

    public Long getRestaurantDishServerId() { return restaurantDishServerId; }
    public void setRestaurantDishServerId(Long restaurantServerId) {
        this.restaurantDishServerId = restaurantServerId;
    }

    public Long getDishPresentationServerId() { return dishPresentationServerId; }
    public void setDishPresentationServerId(Long dishPresentationServerId) {
        this.dishPresentationServerId = dishPresentationServerId;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) {
        this.position = position;
    }

    public Double getCost() { return cost; }
    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getCostText() { return costText; }
    public void setCostText(String costText) {
        this.costText = costText;
    }
}
