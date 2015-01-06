package com.jasoftsolutions.mikhuna.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.model.Location;

/**
 * Created by pc07 on 28/03/14.
 */
public class MapsUtil {

    public static void startMapsActivity(Context context, Location location) {
        try {
//            String uri=String.format(Locale.ENGLISH, "geo:0,0?q=%f,%f(%s)", location.getLatitude(), location.getLongitude(), Uri.encode(location.getLabel()));
            String uri = "http://maps.google.com/maps?q=" + location.getLatitude()
                    + "," + location.getLongitude(); // + " (" + Uri.encode(location.getLabel()) + ")";
            Intent mapsIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            mapsIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            context.startActivity(mapsIntent);
        } catch (Exception e) {
            ContextUtil.showWarningMessage(context, R.string.warning_geo_action_error);
            ExceptionUtil.handleException(e);
        }
    }

}
