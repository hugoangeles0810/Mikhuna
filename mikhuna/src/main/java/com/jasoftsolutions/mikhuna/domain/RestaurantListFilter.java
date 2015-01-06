package com.jasoftsolutions.mikhuna.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gin on 01/05/14.
 */
public class RestaurantListFilter implements Serializable {

    private Long ubigeoServerId;
    private ArrayList<Long> restaurantCategories;
    private ArrayList<Long> serviceTypes;

    public RestaurantListFilter() {
        restaurantCategories = new ArrayList<Long>();
        serviceTypes = new ArrayList<Long>();
    }

    public Long getUbigeoServerId() {
        return ubigeoServerId;
    }
    public void setUbigeoServerId(Long ubigeoServerId) {
        this.ubigeoServerId = ubigeoServerId;
    }

    public ArrayList<Long> getRestaurantCategories() {
        return restaurantCategories;
    }
    public void setRestaurantCategories(ArrayList<Long> restaurantCategories) {
        this.restaurantCategories = restaurantCategories;
    }

    public ArrayList<Long> getServiceTypes() {
        return serviceTypes;
    }
    public void setServiceTypes(ArrayList<Long> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RestaurantListFilter) {
            RestaurantListFilter other = (RestaurantListFilter)o;

            if (other.getUbigeoServerId() != this.getUbigeoServerId()) {
                return false;
            }

            if (other.getRestaurantCategories() != null && this.getRestaurantCategories() != null) {
                if (!this.getRestaurantCategories().equals(other.getRestaurantCategories())) {
                    return false;
                }
            } else if (other.getRestaurantCategories() == null && this.getRestaurantCategories() == null) {

            } else {
                return false;
            }

            if (other.getServiceTypes() != null && this.getServiceTypes() != null) {
                if (!this.getServiceTypes().equals(other.getServiceTypes())) {
                    return false;
                }
            } else if (other.getServiceTypes() == null && this.getServiceTypes() == null) {

            } else {
                return false;
            }

            return true;
        }
        return false;
    }
}
