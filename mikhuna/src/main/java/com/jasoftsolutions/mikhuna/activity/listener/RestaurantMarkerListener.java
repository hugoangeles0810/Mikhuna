package com.jasoftsolutions.mikhuna.activity.listener;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.jasoftsolutions.mikhuna.activity.BaseActivity;
import com.jasoftsolutions.mikhuna.activity.RestaurantDetailActivity;
import com.jasoftsolutions.mikhuna.activity.fragment.dialog.RestaurantDialogFragment;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;

/**
 * Created by Hugo on 31/01/2015.
 */
public class RestaurantMarkerListener implements
        ClusterManager.OnClusterClickListener<Restaurant>,
        ClusterManager.OnClusterItemClickListener<Restaurant> {

    private GoogleMap map;
    private Context context;
    private BaseActivity activity;

    public RestaurantMarkerListener(Context ctx, GoogleMap googleMap, BaseActivity baseActivity){
        context = ctx;
        map = googleMap;
        activity = baseActivity;
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

        RestaurantDialogFragment restaurantDialogFragment = RestaurantDialogFragment.newInstance(restaurant.getServerId());
        restaurantDialogFragment.show(activity.getSupportFragmentManager(), "dialog");

        return true;
    }

}
