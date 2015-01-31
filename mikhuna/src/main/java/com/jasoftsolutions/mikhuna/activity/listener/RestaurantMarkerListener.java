package com.jasoftsolutions.mikhuna.activity.listener;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.jasoftsolutions.mikhuna.activity.RestaurantDetailActivity;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;

/**
 * Created by Hugo on 31/01/2015.
 */
public class RestaurantMarkerListener implements
        ClusterManager.OnClusterClickListener<Restaurant>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Restaurant>,
        ClusterManager.OnClusterItemClickListener<Restaurant> {

    private GoogleMap map;
    private Context context;

    public RestaurantMarkerListener(Context ctx, GoogleMap googleMap){
        context = ctx;
        map = googleMap;
    }

    @Override
    public boolean onClusterClick(Cluster<Restaurant> restaurantCluster) {
        map.animateCamera(CameraUpdateFactory
                .newLatLngZoom(restaurantCluster.getPosition(), map.getCameraPosition().zoom + 2));
        return true;
    }

    @Override
    public boolean onClusterItemClick(Restaurant restaurant) {
        new AuditHelper(context).registerPreviewRestaurantFromMap(restaurant);

        AnalyticsUtil.registerEvent(context, AnalyticsConst.Category.MAP,
                AnalyticsConst.Action.PREVIEW_RESTAURANT_FROM_MAP, restaurant.getServerId().toString());

        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Restaurant restaurant) {
        Intent detailIntent = RestaurantDetailActivity
                .getLauncherIntentByServerId(context, restaurant.getServerId());

        new AuditHelper(context).registerViewRestaurantFromMap(restaurant);

        AnalyticsUtil.registerEvent(context, AnalyticsConst.Category.MAP,
                AnalyticsConst.Action.VIEW_RESTAURANT_FROM_MAP, restaurant.getServerId().toString());

        context.startActivity(detailIntent);
    }
}
