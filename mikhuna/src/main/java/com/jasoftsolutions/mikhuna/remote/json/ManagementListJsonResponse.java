package com.jasoftsolutions.mikhuna.remote.json;

import com.google.gson.annotations.SerializedName;
import com.jasoftsolutions.mikhuna.model.RestaurantCategory;
import com.jasoftsolutions.mikhuna.model.Ubigeo;

import java.io.Serializable;
import java.util.List;

public class ManagementListJsonResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("countRC")
	private Integer restaurantCategoriesCount;

    @SerializedName("countU")
    private Integer ubigeosCount;

	@SerializedName("resultsU")
    private List<Ubigeo> ubigeoResults;

	@SerializedName("resultsRC")
	private List<RestaurantCategory> restCatResults;

    private Long lastUpdate;

    public Integer getRestaurantCategoriesCount() {
        return restaurantCategoriesCount;
    }
    public void setRestaurantCategoriesCount(Integer restaurantCategoriesCount) {
        this.restaurantCategoriesCount = restaurantCategoriesCount;
    }

    public Integer getUbigeosCount() {
        return ubigeosCount;
    }
    public void setUbigeosCount(Integer ubigeosCount) {
        this.ubigeosCount = ubigeosCount;
    }

    public List<Ubigeo> getUbigeoResults() {
        return ubigeoResults;
    }
    public void setUbigeoResults(List<Ubigeo> ubigeoResults) {
        this.ubigeoResults = ubigeoResults;
    }

    public List<RestaurantCategory> getRestCatResults() {
        return restCatResults;
    }
    public void setRestCatResults(List<RestaurantCategory> restCatResults) {
        this.restCatResults = restCatResults;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
