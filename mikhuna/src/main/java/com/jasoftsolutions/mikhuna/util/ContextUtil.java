package com.jasoftsolutions.mikhuna.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.google.gcm.GCMManager;
import com.jasoftsolutions.mikhuna.google.gcm.GCMSendRegIdToBackendTask;

/**
 * Created by pc07 on 11/04/2014.
 */
public class ContextUtil {

    public static void asyncSendGcmToBackend(final Context context) {
        RestaurantListFilterPreferences pref = new RestaurantListFilterPreferences(context);
        if (pref.getUbigeoId() > 0) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        GCMManager gcmManager = new GCMManager(context);
                        GCMSendRegIdToBackendTask task = GCMSendRegIdToBackendTask.getInstance(gcmManager);
                        task.start();
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }

                    return null;
                }
            }.execute();
        }
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }

    public static void showWarningMessage(Context context, int message) {
        showWarningMessage(context, 0, message);
    }

    public static void showWarningMessage(Context context, int title, int message) {
        AlertDialog.Builder b = new AlertDialog.Builder(context);

        if (title > 0) {
            b.setTitle(title);
        }
        b.setMessage(message);
        b.setIcon(android.R.drawable.ic_dialog_alert);

        b.show();
    }

    public static void sendTextIntent(Context context, String text, int title) {
        try {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent, context.getString(title)));
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    public static void navigateWebUrl(Context context, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static long getIntentLongExtra(Intent intent, String name) {
        return getIntentLongExtra(intent, name, 0);
    }

    public static long getIntentLongExtra(Intent intent, String name, long defaultValue) {
        Object result = intent.getExtras().get(name);
        if (result == null) {
            return defaultValue;
        }
        else if (result instanceof Long) {
            return (Long)result;
        } else {
            try {
                return Long.parseLong(result.toString());
            } catch (NumberFormatException nfe) {
                ExceptionUtil.handleException(nfe);
                return defaultValue;
            }
        }
    }
}
