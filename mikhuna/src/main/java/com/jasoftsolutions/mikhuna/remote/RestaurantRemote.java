package com.jasoftsolutions.mikhuna.remote;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.RestaurantDish;
import com.jasoftsolutions.mikhuna.model.RestaurantDishCategory;
import com.jasoftsolutions.mikhuna.model.RestaurantDishPresentation;
import com.jasoftsolutions.mikhuna.model.RestaurantUri;
import com.jasoftsolutions.mikhuna.remote.json.RestaurantListJsonResponse;
import com.jasoftsolutions.mikhuna.remote.json.RestaurantPromotionListJsonResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRemote {

	public RestaurantListJsonResponse getRestaurantList(Long ubigeoId, Long lastUpdate) {
		RestaurantListJsonResponse result;
		
		RemoteHandler sh= RemoteHandler.getInstance();

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        if (ubigeoId != null && ubigeoId > 0) {
            params.add(new BasicNameValuePair("u", ubigeoId.toString()));
        }
        result=sh.getResourceFromUrl("/restaurants/", params, lastUpdate, RestaurantListJsonResponse.class);

		return result;
	}

    public Restaurant getRestaurant(long id) {
        return getRestaurant(id, null);
    }

    public Restaurant getRestaurant(long id, Long lastUpdate) {
        Restaurant result=null;

        RemoteHandler sh= RemoteHandler.getInstance();
        result=sh.getResourceFromUrl("/restaurants/id/"+id+"/", lastUpdate, Restaurant.class);

        return result;
    }

    public RestaurantPromotionListJsonResponse getRestaurantPromotionList(Long ubigeoId, Long lastUpdate) {
        RestaurantPromotionListJsonResponse result = null;

        RemoteHandler sh= RemoteHandler.getInstance();

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        if (ubigeoId != null && ubigeoId > 0) {
            params.add(new BasicNameValuePair("u", ubigeoId.toString()));
        }
        result=sh.getResourceFromUrl("/promotions/", params, lastUpdate, RestaurantPromotionListJsonResponse.class);

        return result;
    }

    public Long getRestaurantServerIdFromUri(String uri) {
        Long result = null;

        RemoteHandler sh= RemoteHandler.getInstance();
        Restaurant response = sh.getResourceFromUrl("/restaurantUrl/url/"+uri+"/", Restaurant.class);

        if (response != null && response.getServerId() != null && response.getServerId() != 0) {
            result = response.getServerId();
        }

        return result;
    }

    public String getRestaurantFullUrlFromServerId(Long serverId) {
        String result = null;

        RemoteHandler sh = RemoteHandler.getInstance();
        RestaurantUri response = sh.getResourceFromUrl("/restaurantId/id/"+serverId+"/", RestaurantUri.class);

        if (response != null && response.getFullUri() != null
                && response.getFullUri().trim().length() > 0) {
            result = response.getFullUri();
        }

        return result;
    }

    public List<RestaurantDishCategory> getRestaurantDishCategoryList(Long restaurantServerId, Long lastUpdate){
        JsonArray result;
        RemoteHandler sh = RemoteHandler.getInstance();

        result = sh.getResourceFromUrl("/products/id/" + restaurantServerId + "/lupd/" + lastUpdate, JsonArray.class);

        return getRestaurantDishCategoryList(restaurantServerId, result);
    }

    public List<RestaurantDishCategory> getRestaurantDishCategoryList(Long restaurantServerId, JsonArray categories) {
        List<RestaurantDishCategory> dishCategories = new ArrayList<RestaurantDishCategory>();
        Gson gson = new Gson();
        for (JsonElement element : categories){
            RestaurantDishCategory dishCategory = gson.fromJson(element.getAsJsonArray().get(0).toString(), RestaurantDishCategory.class);
            dishCategory.setRestaurantServerId(restaurantServerId);
            List<RestaurantDish> restaurantDishes = new ArrayList<RestaurantDish>();
            for (JsonElement elementDish : element.getAsJsonArray().get(1).getAsJsonArray()){
                RestaurantDish dish = gson.fromJson(elementDish.getAsJsonArray().get(0), RestaurantDish.class);
                dish.setDishCategoryServerId(dishCategory.getId());
                dish.setRestaurantServerId(restaurantServerId);
                JsonElement pJson = elementDish.getAsJsonArray().get(1);
                if (pJson.isJsonArray()) {
                    List<RestaurantDishPresentation> presentations = gson.fromJson(pJson, new TypeToken<List<RestaurantDishPresentation>>(){}.getType());
                    if (presentations!=null){
                        for (RestaurantDishPresentation p : presentations){
                            p.setRestaurantDishServerId(dish.getId());
                        }
                    }
                    dish.setDishPresentations(presentations);
                }
                restaurantDishes.add(dish);
            }
            dishCategory.setRestaurantDishes(restaurantDishes);
            dishCategories.add(dishCategory);
        }

        return dishCategories;
    }

//    public List<RestaurantDishCategory> getRestaurantDishCategoryList(Long restaurantServerId, Long lastUpdate){
//        RestaurantDishCategoryListJsonResponse result = getRestaurantDishCategoryListJsonResponse(restaurantServerId, lastUpdate);
//        List<RestaurantDishCategory> dishCategories = new ArrayList<RestaurantDishCategory>();
//
//        for (RestaurantDishCategoryJson dishCategoryJson : result.getResults() ){
//            RestaurantDishCategory dishCategory = dishCategoryJson.getRestaurantDishCategory();
//            dishCategories.add(dishCategory);
//            List<RestaurantDish> restaurantDishes =  new ArrayList<RestaurantDish>();
//            for (RestaurantDishJson restaurantDishJson : dishCategoryJson.getRestaurantDishList()){
//                RestaurantDish  restaurantDish = restaurantDishJson.getRestaurantDish();
//                restaurantDish.setRestaurantServerId(restaurantServerId);
//                restaurantDishes.add(restaurantDish);
//                List<RestaurantDishPresentation> dishPresentations = new ArrayList<RestaurantDishPresentation>();
//                for (RestaurantDishPresentation presentation : restaurantDishJson.getRestaurantDishPresentations()){
//                    presentation.setRestaurantDishServerId(restaurantDish.getDishCategoryServerId());
//                    dishPresentations.add(presentation);
//                }
//                restaurantDish.setDishPresentations(dishPresentations);
//            }
//            dishCategory.setRestaurantDishes(restaurantDishes);
//            dishCategory.setRestaurantServerId(restaurantServerId);
//            dishCategories.add(dishCategory);
//        }
//
//        return dishCategories;
//    }
}
