package com.jasoftsolutions.mikhuna.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Hugo on 28/01/2015.
 */
public class InternetUtil {

    public static boolean isInternetConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo info: infos){
                if (info.isConnected()) return true;
            }
        }

        return false;

    }

}
