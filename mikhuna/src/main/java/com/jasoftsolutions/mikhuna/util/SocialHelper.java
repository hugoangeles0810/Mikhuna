package com.jasoftsolutions.mikhuna.util;

import android.content.Context;

import com.facebook.AppEventsLogger;
import com.jasoftsolutions.mikhuna.remote.Const;

/**
 * Created by mgaray on 02/10/14.
 */
public class SocialHelper {

    public static void activateAppFacebookEventLog(Context context) {
        AppEventsLogger.activateApp(context, Const.FACEBOOK_APP_ID);
    }

}
