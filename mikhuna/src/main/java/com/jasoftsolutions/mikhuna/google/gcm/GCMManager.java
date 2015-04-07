package com.jasoftsolutions.mikhuna.google.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jasoftsolutions.mikhuna.BuildConfig;
import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.remote.RemoteHandler;
import com.jasoftsolutions.mikhuna.remote.json.JsonResponse;
import com.jasoftsolutions.mikhuna.util.AccountUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc07 on 02/04/2014.
 */
public class GCMManager {

    private static final String TAG = GCMManager.class.getSimpleName();

    private Context context;
    private SharedPreferences gcmPref;

    private GoogleCloudMessaging gcm;

    public GCMManager(Context context) {
        this.context = context;
        gcmPref = GCMPreferences.get(context);
        gcm = GoogleCloudMessaging.getInstance(context);
    }

    public void setup() {
        if (checkPlayServices()) {
            Log.i(TAG, "Current registration ID: "+getCurrentRegId());
            if (!isValidRegId()) {
                Log.i(TAG, "GCM Registration ID not found or invalid.");
                GCMRegisterTask registerTask = GCMRegisterTask.getInstance(this);
                registerTask.start();
            } else {
                // si falta completar la tarea de envío del ID al backend
                if (!hasCurrentRegIdSentToBackend()) {
                    Log.i(TAG, "Reattempt to send Reg Id to backend...");
                    GCMSendRegIdToBackendTask gcmSendRegIdToBackendTask = GCMSendRegIdToBackendTask.getInstance(this);
                    gcmSendRegIdToBackendTask.start();
                }
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    public String registerToGCMService() throws IOException {
        String regId = gcm.register(Const.GOOGLE_APP_ID);
        gcmPref.edit()
            .putString(GCMPreferences.REGISTRATION_ID, regId)
            .putString(GCMPreferences.USER, AccountUtil.getDefaultGoogleAccount(context))
            .putInt(GCMPreferences.APP_VERSION, BuildConfig.VERSION_CODE)
            .commit();
        return regId;
    }

    public boolean sendRegIdToBackend() {
        RemoteHandler sh = new RemoteHandler();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", getCurrentRegId()));
        params.add(new BasicNameValuePair("ue", getCurrentRegisteredUser()));
//        params.add(new BasicNameValuePair("v", String.valueOf(getCurrentRegisteredAppVersion())));
        params.add(new BasicNameValuePair("u", String.valueOf(new RestaurantListFilterPreferences(context).getUbigeoId())));
        JsonResponse response = sh.postResourceFromUrl("/gcmRegId/", params, JsonResponse.class);
        if (response!=null && response.getSuccess()) {
            gcmPref.edit()
                .putString(GCMPreferences.LAST_REGID_SENT, BuildConfig.VERSION_CODE
                        + "_" + getCurrentRegId())
                .commit();
            return true;
        } else {
            return false;
        }
    }

    private boolean checkPlayServices() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    public String getCurrentRegId() {
        return gcmPref.getString(GCMPreferences.REGISTRATION_ID, null);
    }

    public String getCurrentRegisteredUser() {
        return gcmPref.getString(GCMPreferences.USER, null);
    }

    public int getCurrentRegisteredAppVersion() {
        return gcmPref.getInt(GCMPreferences.APP_VERSION, 0);
    }

    public String getLastRegIdSentToBackend() {
        return gcmPref.getString(GCMPreferences.LAST_REGID_SENT, null);
    }

    public boolean isValidRegId() {
        String regId=getCurrentRegId();
        if (regId==null) {
            return false;
        }

        String registeredUser = getCurrentRegisteredUser();
        if (registeredUser == null || !registeredUser.equals(AccountUtil.getDefaultGoogleAccount(context))) {
            return false;
        }

        int registeredAppVersion = getCurrentRegisteredAppVersion();
        if (registeredAppVersion != BuildConfig.VERSION_CODE) {
            return false;
        }

//        long expirationDate = gcmPref.getLong(GCMPreferences.EXPIRATION_DATE, 0);
//        if (System.currentTimeMillis() > expirationDate) {
//            return null;
//        }

        return true;
    }

    public boolean hasCurrentRegIdSentToBackend() {
        String lastRegIdSentToBackend = getLastRegIdSentToBackend();
        if (lastRegIdSentToBackend != null) {
            return getLastRegIdSentToBackend().equals(BuildConfig.VERSION_CODE
                    + "_" + getCurrentRegId());
        } else {
            return false;
        }
    }

}
