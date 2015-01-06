package com.jasoftsolutions.mikhuna.routing;

import android.content.Context;

import com.jasoftsolutions.mikhuna.activity.ArgKeys;
import com.jasoftsolutions.mikhuna.activity.RestaurantDetailActivity;
import com.usepropeller.routable.Router;

/**
 * Created by pc07 on 17/10/2014.
 */
public class Routes {

    public static void setContext(Context context) {
        Router.sharedRouter().setContext(context);
    }

    public static void setup(Context context) {
        Router r = Router.sharedRouter();

        r.setContext(context);

        r.map("restaurantes/detail/id/:"+ArgKeys.RESTAURANT_SERVER_ID, RestaurantDetailActivity.class);
        r.map("Restaurant/detail/id/:"+ArgKeys.RESTAURANT_SERVER_ID, RestaurantDetailActivity.class);
        r.map("Restaurant/:"+ArgKeys.RESTAURANT_SERVER_ID, RestaurantDetailActivity.class);
        r.map("restaurantes/:"+ArgKeys.RESTAURANT_SERVER_ID, RestaurantDetailActivity.class);
        r.map("restaurantes/:"+ArgKeys.CITY+"/:"+ArgKeys.RESTAURANT_URI, RestaurantDetailActivity.class);
        r.map("Link/:"+ArgKeys.RESTAURANT_SERVER_ID, RestaurantDetailActivity.class);
    }

}
