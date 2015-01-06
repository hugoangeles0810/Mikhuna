package com.jasoftsolutions.mikhuna.google.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by pc07 on 02/04/2014.
 */
public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName componentName = new ComponentName(context.getPackageName(),
                GCMIntentService.class.getName());
        startWakefulService(context, intent.setComponent(componentName));
        setResultCode(Activity.RESULT_OK);
    }

}
