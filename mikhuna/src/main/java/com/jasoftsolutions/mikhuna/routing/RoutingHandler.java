package com.jasoftsolutions.mikhuna.routing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.usepropeller.routable.Router;

/**
 * Created by pc07 on 17/10/2014.
 */
public class RoutingHandler {

    /**
     * True si hay rutas que manejar, sino retorna false
     * @param intent
     * @return
     */
    public static boolean handleRouter(Context context, Intent intent) {
        Routes.setContext(context);
        Uri uri = intent.getData();
        if (uri != null) {
            try {
                Router.sharedRouter().open(uri.getPath());
                return true;
            } catch (Exception e) {
                ExceptionUtil.handleException(e);
            }
        }
        return false;
    }

}
