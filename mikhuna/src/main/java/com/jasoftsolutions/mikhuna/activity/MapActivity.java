package com.jasoftsolutions.mikhuna.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.fragment.dialog.Dialogs;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.store.RestaurantStore;
import com.jasoftsolutions.mikhuna.store.StoreListener;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;
import com.jasoftsolutions.mikhuna.util.LocationUtil;
import com.jasoftsolutions.mikhuna.util.ResourcesUtil;
import com.jasoftsolutions.mikhuna.util.StringUtil;

import java.util.ArrayList;

public class MapActivity extends BaseActivity implements
        ClusterManager.OnClusterClickListener<Restaurant>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Restaurant>,
        ClusterManager.OnClusterItemClickListener<Restaurant>,
        StoreListener{

    public static final float DEFAULT_ZOOM  = 6;
    private ArrayList<Restaurant> restaurants;
    private ClusterManager<Restaurant> restaurantsCluster;
    private GoogleMap map;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (status != ConnectionResult.SUCCESS){
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }else{

            setup();

            if (savedInstanceState == null){
                RestaurantStore rs = RestaurantStore.getInstance();
                showProgressDialog();
                rs.requestAllRestaurants(this);
                defaultCameraPositionAnimate();
            }else{
                restaurants = savedInstanceState.getParcelableArrayList(ArgKeys.RESTAURANTS);
                if (restaurants!=null){
                    restaurantsCluster.addItems(restaurants);
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        RestaurantStore rs = RestaurantStore.getInstance();
        rs.addListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RestaurantStore rs = RestaurantStore.getInstance();
        rs.removeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ArgKeys.RESTAURANTS, restaurants);
        super.onSaveInstanceState(outState);
    }

    private void setUpMapIfNeeded() {
        if (map != null) {
            return;
        }
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    }

    private void setup(){
        setUpMapIfNeeded();
        restaurantsCluster = new ClusterManager<Restaurant>(this, map);
        map.setMyLocationEnabled(true);
        map.setOnCameraChangeListener(restaurantsCluster);
        map.setOnMarkerClickListener(restaurantsCluster);
        map.setOnInfoWindowClickListener(restaurantsCluster);
        restaurantsCluster.setOnClusterClickListener(this);
        restaurantsCluster.setOnClusterItemClickListener(this);
        restaurantsCluster.setOnClusterItemInfoWindowClickListener(this);
        restaurantsCluster.setRenderer(new RestaurantRenderer());
    }

    private void defaultCameraPositionAnimate(){
        LatLng latLng = LocationUtil.getLastKnowLocation(this);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }


    @Override
    public boolean onClusterClick(Cluster<Restaurant> restaurantCluster) {
        map.animateCamera(CameraUpdateFactory
                .newLatLngZoom(restaurantCluster.getPosition(), map.getCameraPosition().zoom + 2));
        return true;
    }

    @Override
    public boolean onClusterItemClick(Restaurant restaurant) {
        new AuditHelper(this).registerPreviewRestaurantFromMap(restaurant);

        AnalyticsUtil.registerEvent(this, AnalyticsConst.Category.MAP,
                AnalyticsConst.Action.PREVIEW_RESTAURANT_FROM_MAP, restaurant.getServerId().toString());

        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Restaurant restaurant) {
        Intent detailIntent = RestaurantDetailActivity
                .getLauncherIntentByServerId(this, restaurant.getServerId());

        new AuditHelper(this).registerViewRestaurantFromMap(restaurant);

        AnalyticsUtil.registerEvent(this, AnalyticsConst.Category.MAP,
                AnalyticsConst.Action.VIEW_RESTAURANT_FROM_MAP, restaurant.getServerId().toString());

        startActivity(detailIntent);
    }


    protected GoogleMap getMap() {
        setUpMapIfNeeded();
        return map;
    }

    public void showProgressDialog(){
        progressDialog = ProgressDialog.show(MapActivity.this,
                null,
                StringUtil.getStringForResourceId(MapActivity.this, R.string.search_restaurants_map)
                , true);
        progressDialog.show();
    }

    @Override
    public void onReady(Object sender, Object data) {

    }

    @Override
    public void onUpdate(Object sender, Object data) {

    }

    @Override
    public void onTimeOut(Object sender, @Nullable Object data) {

    }

    @Override
    public void onFailedConnection(Object sender, @Nullable Object data) {

    }

    @Override
    public void onSuccess(Object sender,final Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                restaurantsCluster.clearItems();
                restaurants = (ArrayList<Restaurant>) data;
                restaurantsCluster.addItems(restaurants);
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onFail(Object sender,final Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Dialogs.noInternetConnectionMessage(MapActivity.this).show();
            }
        });
    }


    public static Intent getLauncherIntent(Context context){
        return new Intent(context, MapActivity.class);
    }

    // Marker Personalizado
    class RestaurantRenderer extends DefaultClusterRenderer<Restaurant>{
        public RestaurantRenderer() {
            super(getApplicationContext(), getMap(), restaurantsCluster);

        }

        @Override
        protected void onBeforeClusterItemRendered(Restaurant item, MarkerOptions markerOptions) {
            markerOptions.title(item.getName())
            .snippet(StringUtil.getStringForResourceId(MapActivity.this, R.string.detail_marker_map))
            .icon(BitmapDescriptorFactory.fromResource(
                    ResourcesUtil.getDrawableIdentifierByName(MapActivity.this, "type_restaurant_" + item.getImageId())
            ));
        }
    }
}
