package com.jasoftsolutions.mikhuna.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.jasoftsolutions.mikhuna.BuildConfig;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.RestaurantDish;
import com.jasoftsolutions.mikhuna.model.RestaurantDishCategory;
import com.jasoftsolutions.mikhuna.model.RestaurantDishPresentation;
import com.jasoftsolutions.mikhuna.model.RestaurantUri;
import com.jasoftsolutions.mikhuna.model.TempLikeDish;
import com.jasoftsolutions.mikhuna.remote.json.DishCategoriesResponseData;
import com.jasoftsolutions.mikhuna.remote.json.RestaurantListJsonResponse;
import com.jasoftsolutions.mikhuna.remote.json.RestaurantPromotionListJsonResponse;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

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

    public ArrayList<Restaurant> getAllRestaurants(){
        ArrayList<Restaurant> result;
        Gson gson = new Gson();
        JsonElement json;
        RemoteHandler sh= RemoteHandler.getInstance();

        json = sh.getResourceFromUrl("/restaurantLocation/", JsonElement.class);
        result = gson.fromJson(json, new TypeToken<List<Restaurant>>(){}.getType());

        return result;
    }

    public Restaurant getRestaurant(long id) {
        return getRestaurant(id, null);
    }

    public Restaurant getRestaurant(long id, Long lastUpdate) {
        Restaurant result=null;

        RemoteHandler sh= RemoteHandler.getInstance();
        result=sh.getResourceFromUrl("/restaurants/id/"+id+"/v/"+ BuildConfig.VERSION_CODE, lastUpdate, Restaurant.class);

        return result;
    }

    public Long getIdOfRestaurantRecommended(Long ubigeoId, String user){
        Long id;
        RemoteHandler sh = RemoteHandler.getInstance();

        id = sh.getResourceFromUrl("/recommend/user/" + user + "/u/" + ubigeoId + "/v/" + BuildConfig.VERSION_CODE, Long.class);

        return id;

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

    public DishCategoriesResponseData getRestaurantDishCategoryList(Long restaurantServerId, Long lastUpdate, Long lastUpdateP, String user){
        JsonElement result;
        RemoteHandler sh = RemoteHandler.getInstance();

        result = sh.getResourceFromUrl("/products/id/" + restaurantServerId +
                "/lupd/"+ lastUpdate +
                "/lupdp/" + lastUpdateP +
                "/user/" + user+
                "/v/" + BuildConfig.VERSION_CODE
                , JsonElement.class);

        return getDishCategoryResponseData(restaurantServerId, result);
    }

    private DishCategoriesResponseData getDishCategoryResponseData(Long restaurantServerId, JsonElement result) {
        DishCategoriesResponseData responseData = new DishCategoriesResponseData();
        Gson gson = new Gson();
        responseData.setLastUpdate(gson.fromJson(result.getAsJsonObject().get("lastUpdate"), Long.class));
        responseData.setCount(gson.fromJson(result.getAsJsonObject().get("count"), Integer.class));
        responseData.setCategories(getRestaurantDishCategoryList(restaurantServerId, result.getAsJsonObject().get("results")));
        responseData.setCountLikeProduct(gson.fromJson(result.getAsJsonObject().get("countLikeProduct"), Integer.class));
        ArrayList<RestaurantDish> dishesLike = gson.fromJson(result.getAsJsonObject().get("resultsLikeProduct"), new TypeToken<ArrayList<RestaurantDish>>(){}.getType());
        responseData.setResultsLikeProduct(dishesLike);
        return responseData;
    }

    public ArrayList<RestaurantDishCategory> getRestaurantDishCategoryList(Long restaurantServerId, JsonElement categories) {
        if (categories == null || categories.isJsonNull() ) { return null; }
        ArrayList<RestaurantDishCategory> dishCategories = new ArrayList<RestaurantDishCategory>();
        Gson gson = new Gson();
        for (JsonElement element : categories.getAsJsonArray()){
            RestaurantDishCategory dishCategory = gson.fromJson(element.getAsJsonArray().get(0), RestaurantDishCategory.class);
            dishCategory.setRestaurantServerId(restaurantServerId);
            ArrayList<RestaurantDish> restaurantDishes = new ArrayList<RestaurantDish>();
            for (JsonElement elementDish : element.getAsJsonArray().get(1).getAsJsonArray()){
                RestaurantDish dish = gson.fromJson(elementDish.getAsJsonArray().get(0), RestaurantDish.class);
                dish.setDishCategoryServerId(dishCategory.getServerId());
                dish.setRestaurantServerId(restaurantServerId);
                JsonElement pJson = elementDish.getAsJsonArray().get(1);
                if (pJson.isJsonArray()) {
                    List<RestaurantDishPresentation> presentations = gson.fromJson(pJson, new TypeToken<List<RestaurantDishPresentation>>(){}.getType());
                    if (presentations!=null){
                        for (RestaurantDishPresentation p : presentations){
                            p.setRestaurantDishServerId(dish.getServerId());
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

    public boolean sendLikeDish(List<TempLikeDish> dishList, String mail){
        RemoteHandler sh = RemoteHandler.getInstance();
        JsonElement response = null;
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.addProperty("u", mail);
        json.add("data", gson.toJsonTree(dishList));

        List<NameValuePair> params = new ArrayList();
        NameValuePair likes = new BasicNameValuePair("likes", json.toString());
        params.add(likes);

        try{
            response = sh.postResourceFromUrl("/saveLike/", params, JsonElement.class);
            if (response != null &&
                    gson.fromJson(response.getAsJsonObject().get("success"), Boolean.class)){
                return true;
            }
        }catch (Exception e){
            ExceptionUtil.ignoreException(e);
        }

        return false;

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
