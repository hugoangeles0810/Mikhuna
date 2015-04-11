package com.jasoftsolutions.mikhuna.remote;

import android.content.Context;
import android.content.SharedPreferences;

import com.jasoftsolutions.mikhuna.data.ManagementManager;
import com.jasoftsolutions.mikhuna.remote.json.ManagementListJsonResponse;

/**
 * Created by pc07 on 02/04/2014.
 */
public class ManagementUpdaterThread extends Thread {

    private static final String THREAD_NAME = ManagementUpdaterThread.class.getName();
    private static final String TAG = ManagementUpdaterThread.class.getSimpleName();

    private Context context;
    private Boolean result;

    private static ManagementUpdaterThread instance;

    private ManagementUpdaterThread(Context context) {
        super(THREAD_NAME);
        this.context = context;
    }

    public static ManagementUpdaterThread getInstance(Context context) {
        if (instance==null || !instance.isAlive()) {
            instance = new ManagementUpdaterThread(context);
        }
        return instance;
    }

    public void startIfNecessary() {
        if (!isAlive()) start();
    }

    @Override
    public void run() {
        result = null;

        ManagementRemote service=new ManagementRemote();

        LastResourceUpdatePreferences lru = new LastResourceUpdatePreferences(context);
        long lastUpdate=lru.getManagementLastUpdate();

        ManagementListJsonResponse response = service.getManagementList(lastUpdate);

        if (response!=null) {
            ManagementManager manager=new ManagementManager();
            manager.saveUbigeos(response.getUbigeoResults());
            manager.saveRestCategories(response.getRestCatResults());

            lru.setManagementLastUpdate(response.getLastUpdate());
            lru.setVersion(response.getVersion());

            result = true;
        } else {
            result = false;
        }

        instance=null;
    }

    public Boolean getResult() {
        return result;
    }
}
