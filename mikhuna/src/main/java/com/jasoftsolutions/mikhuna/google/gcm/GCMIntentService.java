package com.jasoftsolutions.mikhuna.google.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.ArgKeys;
import com.jasoftsolutions.mikhuna.activity.MainActivity;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

/**
 * Created by pc07 on 02/04/2014.
 */
public class GCMIntentService extends IntentService {

    private static final String TAG = GCMIntentService.class.getSimpleName();
//    private static final String SERVICE_NAME = "MikhunaGCMIntentService";
    private static final String SERVICE_NAME = Const.GOOGLE_APP_ID;

    private static final String EXTRAS_MSG = "msg";
    private static final String EXTRAS_ACTION = "action";
    private static final String EXTRAS_DESCRIPTION = "description";
    private static final String EXTRAS_RESTAURANT_ID = "rest_id";

    public GCMIntentService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Bundle extras = intent.getExtras();

            Log.i(TAG, "Recibiendo una notificación GCM: "+extras.toString() + "; " + extras.describeContents());

            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

            String messageType = gcm.getMessageType(intent);

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
    //            notifyGCMMsg("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
    //            notifyGCMMsg("Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)){
                int action = 0;
                if (extras.containsKey(EXTRAS_ACTION)) {
                    try {
                        action = Integer.valueOf(extras.getString(EXTRAS_ACTION));
                    } catch (NumberFormatException nfe) {
                        ExceptionUtil.handleException(nfe);
                    }
                }
                if (action>=0) {
                    GCMNotificationAction[] allActions = GCMNotificationAction.values();
                    GCMNotificationAction gcmNotificationAction = allActions[action];

                    Bundle options = new Bundle();
                    options.putInt(ArgKeys.SOURCE, ArgKeys.SOURCE_GCM);

                    if (extras.containsKey(EXTRAS_DESCRIPTION)) {
                        options.putString(ArgKeys.MESSAGE, extras.getString(EXTRAS_DESCRIPTION));
                    }

                    String message = extras.getString(EXTRAS_MSG);

                    if (gcmNotificationAction.equals(GCMNotificationAction.SIMPLE)) {
    //                    notifyGCMMsg(message, MainActivity.class, options);
                    } else if (gcmNotificationAction.equals(GCMNotificationAction.RESTAURANT)) {
                        if (extras.containsKey(EXTRAS_RESTAURANT_ID)) {
                            long restaurantId = 0;
                            try {
                                restaurantId = Long.valueOf(extras.getString(EXTRAS_RESTAURANT_ID));
                                options.putLong(ArgKeys.RESTAURANT_ID, restaurantId);
                            } catch (NumberFormatException nfe) {
                                ExceptionUtil.handleException(nfe);
                            }
                        }
                    }
                    notifyGCMMsg(message, MainActivity.class, options);
                }
            }

            GCMBroadcastReceiver.completeWakefulIntent(intent);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    private <T extends FragmentActivity> void notifyGCMMsg(String msg, Class<T> activityClass, Bundle options) {
        try {
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            Intent activityIntent = new Intent(this, activityClass);
            activityIntent.putExtras(options);

            PendingIntent pendingIntent = PendingIntent.getActivity
                    (this, 0, activityIntent, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            int defaults = 0;
            defaults |= Notification.DEFAULT_SOUND;
//            defaults |= Notification.DEFAULT_VIBRATE;

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentTitle(this.getString(R.string.app_name))
                    .setContentText(msg)
                    .setContentIntent(pendingIntent)
                    .setDefaults(defaults)
            ;

            notificationManager.notify(R.id.notification_gcm_msg, builder.build());
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

}
