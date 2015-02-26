package com.jasoftsolutions.mikhuna.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.model.TempLikeDish;
import com.jasoftsolutions.mikhuna.remote.RestaurantRemote;
import com.jasoftsolutions.mikhuna.util.AccountUtil;

import java.util.List;

/**
 * Created by Hugo on 26/02/2015.
 */
public class ServiceSendLikeDish extends IntentService {

    public static final String TAG = ServiceSendLikeDish.class.getSimpleName();
    public static final String SERVICE_NAME = ServiceSendLikeDish.class.getSimpleName();
    public static final String ACTION_SEND_LIKE = "action_send_like";

    public ServiceSendLikeDish() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(ACTION_SEND_LIKE)){
            Log.i(TAG, ">> Inicio envio de likes");
            RestaurantManager rm = new RestaurantManager();
            List<TempLikeDish> tempLikeDishs = rm.getAllTempLikeDish();
            String mail = AccountUtil.getDefaultGoogleAccount(this);
            RestaurantRemote remote = new RestaurantRemote();

            if (tempLikeDishs != null && !tempLikeDishs.isEmpty()){
                boolean rpta;

                Log.i(TAG, "Enviando likes ... ");
                rpta = remote.sendLikeDish(tempLikeDishs, mail);

                if (rpta){
                    Log.e(TAG, "Exito se registraron los likes");
                    rm.deleteAllTempLikeDish();
                }


            }
            Log.i(TAG, "<< Fin envio de likes");

        }
    }

    public static Intent getIntentOfActionSendLike(Context context){
        Intent intent = new Intent(context, ServiceSendLikeDish.class);
        intent.setAction(ACTION_SEND_LIKE);
        return intent;
    }
}
