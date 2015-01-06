package com.jasoftsolutions.mikhuna.google.gcm;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pc07 on 02/04/2014.
 */
public final class GCMPreferences {

    public static final String REGISTRATION_ID="reg_id";
    public static final String USER="user";
    public static final String APP_VERSION="app_version";
    public static final String LAST_REGID_SENT="last_regid_sent";
//    public static final String EXPIRATION_DATE="expiration_date";


    public static final SharedPreferences get(Context context) {
        return context.getSharedPreferences("gcm", Context.MODE_PRIVATE);
    }

}
