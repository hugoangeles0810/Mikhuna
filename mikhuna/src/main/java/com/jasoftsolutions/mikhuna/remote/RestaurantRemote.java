package com.jasoftsolutions.mikhuna.remote;

import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.RestaurantUri;
import com.jasoftsolutions.mikhuna.remote.json.RestaurantListJsonResponse;
import com.jasoftsolutions.mikhuna.remote.json.RestaurantPromotionListJsonResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

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
}
