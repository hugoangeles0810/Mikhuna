package com.jasoftsolutions.mikhuna.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.remote.RestaurantRemote;
import com.jasoftsolutions.mikhuna.util.LocationUtil;
import com.jasoftsolutions.mikhuna.util.ResourcesUtil;
import com.jasoftsolutions.mikhuna.util.StringUtil;

import java.util.ArrayList;

public class MapActivity extends BaseActivity implements
        ClusterManager.OnClusterClickListener<Restaurant>, ClusterManager.OnClusterItemInfoWindowClickListener<Restaurant>  {

    public static final float DEFAULT_ZOOM  = 6;
    private ArrayList<Restaurant> restaurants;
    private ClusterManager<Restaurant> restaurantsCluster;
    private GoogleMap map;

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
                new AsyncRestaurantMarker().execute();
                defaulf
            }else{
                if (savedInstanceState.containsKey(ArgKeys.RESTAURANTS)){
                    restaurants = savedInstanceState.getParcelableArrayList(ArgKeys.RESTAURANTS);
                    restaurantsCluster.addItems(restaurants);
                }else{
                    new AsyncRestaurantMarker().execute();
                }
            }
        }

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
        restaurantsCluster.setOnClusterItemInfoWindowClickListener(this);
        restaurantsCluster.setRenderer(new RestaurantRenderer());
    }

    private CameraPosition defaultCameraPositionAnimate(){
        LatLng latLng = LocationUtil.getLastKnowLocation(this);

        CameraPosition cp = CameraPosition.builder()
                .target(latLng)
                .zoom(DEFAULT_ZOOM)
                .build();

        return cp;
    }


    @Override
    public boolean onClusterClick(Cluster<Restaurant> restaurantCluster) {
        map.animateCamera(CameraUpdateFactory
                .newLatLngZoom(restaurantCluster.getPosition(), map.getCameraPosition().zoom + 2));
        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(Restaurant restaurant) {
        Intent detailIntent = RestaurantDetailActivity
                .getLauncherIntentByServerId(this, restaurant.getServerId());
        startActivity(detailIntent);
    }


    protected GoogleMap getMap() {
        setUpMapIfNeeded();
        return map;
    }

    // Solicita los restaurantes
    class AsyncRestaurantMarker extends AsyncTask<Void, Void, ArrayList<Restaurant>>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MapActivity.this,
                    null,
                    StringUtil.getStringForResourceId(MapActivity.this, R.string.search_restaurants_map)
                    , true);
            progressDialog.show();
        }

        @Override
        protected ArrayList<Restaurant> doInBackground(Void... params) {
            RestaurantRemote rr = new RestaurantRemote();
            ArrayList<Restaurant> restaurants =  rr.getAllRestaurants();
            return restaurants;
        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> result) {
            super.onPostExecute(restaurants);
            restaurants = result;
            restaurantsCluster.addItems(restaurants);
            progressDialog.dismiss();
        }
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
