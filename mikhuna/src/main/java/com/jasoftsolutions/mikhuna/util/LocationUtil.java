package com.jasoftsolutions.mikhuna.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;
import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.data.ManagementManager;
import com.jasoftsolutions.mikhuna.model.Ubigeo;

/**
 * Created by Hugo on 29/01/2015.
 */
public class LocationUtil {

    public static final Long DEFAULT_UBIGEO_ID = 6l;
    public static final Double DEFAULT_LATITUDE = -12.075700952290482;
    public static final Double DEFAULT_LONGITUDE = -77.04816375717775;

    public static LatLng getLastKnowLocation(Context context){
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        LatLng position;
        Location location = null;

        if (locationManager != null){
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (location != null){
            position = new LatLng(location.getLatitude(), location.getLongitude());
        }else{
            position = getPositionByUbigeoServerIdOrDefaultPosition(context);
        }

        return position;
    }

    private static LatLng getPositionByUbigeoServerIdOrDefaultPosition(Context context){
        Long ubigeoId = new RestaurantListFilterPreferences(context).getUbigeoId();
        ManagementManager mm = new ManagementManager();
        Ubigeo ubigeo = mm.getUbigeoByServerId(ubigeoId);
        LatLng latLng;

        if (ubigeo != null) {
            latLng = new LatLng(ubigeo.getLatitude(), ubigeo.getLongitude());
        }else{
            latLng = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        }

        return latLng;
    }

}
