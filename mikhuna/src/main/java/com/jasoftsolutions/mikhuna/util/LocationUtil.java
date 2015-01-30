package com.jasoftsolutions.mikhuna.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

import java.nio.DoubleBuffer;

/**
 * Created by Hugo on 29/01/2015.
 */
public class LocationUtil {

    public static final Double DEFAULT_LATITUDE = -9.1951786;
    public static final Double DEFAULT_LONGITUDE = -74.99041651;

    public static LatLng getLastKnowLocation(Context context){
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        LatLng position;
        Location location = null;

        if (locationManager != null){
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null){
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }

        if (location != null){
            position = new LatLng(location.getLatitude(), location.getLongitude());
        }else{
            position = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        }

        return position;
    }

}
