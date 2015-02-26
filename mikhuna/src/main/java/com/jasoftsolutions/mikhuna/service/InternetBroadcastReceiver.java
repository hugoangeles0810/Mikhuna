package com.jasoftsolutions.mikhuna.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jasoftsolutions.mikhuna.util.InternetUtil;

/**
 * Created by Hugo on 26/02/2015.
 */
public class InternetBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (InternetUtil.isInternetConnected(context)){
            context.startService(ServiceSendLikeDish.getIntentOfActionSendLike(context));
        }
    }

}
