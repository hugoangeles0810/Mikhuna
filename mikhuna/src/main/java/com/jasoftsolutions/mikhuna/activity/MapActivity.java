package com.jasoftsolutions.mikhuna.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantListFilterFragment;
import com.jasoftsolutions.mikhuna.activity.fragment.dialog.Dialogs;
import com.jasoftsolutions.mikhuna.activity.fragment.dialog.MapFilterDialog;
import com.jasoftsolutions.mikhuna.activity.listener.ApplyActionListener;
import com.jasoftsolutions.mikhuna.activity.listener.RestaurantMarkerListener;
import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.domain.RestaurantListFilter;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.RestaurantCategoryAssign;
import com.jasoftsolutions.mikhuna.store.RestaurantStore;
import com.jasoftsolutions.mikhuna.store.StoreListener;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;
import com.jasoftsolutions.mikhuna.util.ArrayUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.LocationUtil;
import com.jasoftsolutions.mikhuna.util.ResourcesUtil;
import com.jasoftsolutions.mikhuna.util.StringUtil;

import java.util.ArrayList;

public class MapActivity extends BaseActivity implements
        GoogleMap.OnMapLoadedCallback,
        StoreListener{

    public static final String TAG = MapActivity.class.getSimpleName();

    public static final float DEFAULT_ZOOM  = 15;
    private ArrayList<Restaurant> restaurants;
    private ClusterManager<Restaurant> restaurantsCluster;
    private GoogleMap map;
    private ProgressDialog progressDialog;
    private RestaurantMarkerListener restaurantMarkerListener;
    private MapFilterDialog filterDialog;

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

            if(savedInstanceState != null){
                try {
                    restaurants = savedInstanceState.getParcelableArrayList(ArgKeys.RESTAURANTS);
                }catch (Exception e){
                    restaurants = null;
                }

                if (restaurants!=null){
                    restaurantsCluster.addItems(restaurants);
                }else{
                    RestaurantStore rs = RestaurantStore.getInstance();
                    rs.requestAllRestaurants(this);
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        RestaurantStore rs = RestaurantStore.getInstance();
        rs.addListener(this);
        filterRestaurants();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh){
            requestRestaurantsOrRefresh();
            return true;
        }

        if (id == R.id.action_filter){
            Intent i = FilterMapActivity.getLauncherIntent(this);
            startActivity(i);
        }

        return false;
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
        restaurantMarkerListener = new RestaurantMarkerListener(this, getMap(), this);
        map.setMyLocationEnabled(true);
        map.setOnMapLoadedCallback(this);
        map.setOnCameraChangeListener(restaurantsCluster);
        map.setOnMarkerClickListener(restaurantsCluster);
        restaurantsCluster.setOnClusterClickListener(restaurantMarkerListener);
        restaurantsCluster.setOnClusterItemClickListener(restaurantMarkerListener);
        restaurantsCluster.setRenderer(new RestaurantRenderer());
    }

    private void defaultCameraPositionAnimate(){
        LatLng latLng = LocationUtil.getLastKnowLocation(this);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
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

    public void requestRestaurantsOrRefresh(){
        RestaurantStore rs = RestaurantStore.getInstance();
        showProgressDialog();
        rs.requestAllRestaurants(this);
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
                try {
                    restaurants = (ArrayList<Restaurant>) data;
                    filterRestaurants();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }catch (Exception e){
                    ExceptionUtil.ignoreException(e);
                }
            }
        });
    }

    @Override
    public void onFail(Object sender,final Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog!=null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                    Dialogs.noInternetConnectionMessage(MapActivity.this).show();
                }

            }
        });
    }


    public static Intent getLauncherIntent(Context context){
        return new Intent(context, MapActivity.class);
    }

    @Override
    public void onMapLoaded() {
        requestRestaurantsOrRefresh();
        defaultCameraPositionAnimate();
    }


    private void filterRestaurants(){
        if (restaurants != null ) {
            try {
                RestaurantListFilterPreferences pref =
                        new RestaurantListFilterPreferences(MapActivity.this,
                                RestaurantListFilterPreferences.PREF_MAP);
                RestaurantListFilter filter = pref.loadFilter();
                ArrayList<Long> categories = filter.getRestaurantCategories();
                ArrayList<Long> services = filter.getServiceTypes();
                restaurantsCluster.clearItems();
                restaurantsCluster.addItems(
                        ArrayUtil.filterFromCategoriesAndServices(restaurants,
                                categories,
                                services));
                restaurantsCluster.cluster();
            }catch (NullPointerException e){
                ExceptionUtil.ignoreException(e);
            }
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
