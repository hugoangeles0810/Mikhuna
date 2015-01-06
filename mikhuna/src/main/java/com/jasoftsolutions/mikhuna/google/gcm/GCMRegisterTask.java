package com.jasoftsolutions.mikhuna.google.gcm;

import android.util.Log;

import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

import java.io.IOException;

/**
 * Created by pc07 on 09/04/2014.
 */
public class GCMRegisterTask extends Thread {

    private static final String TAG = GCMRegisterTask.class.getSimpleName();

    private static GCMRegisterTask singletonThread;

    private Boolean success;
    private Exception resultException;

    private GCMManager gcmManager;

    private GCMRegisterTask(GCMManager gcmManager) {
        this.gcmManager = gcmManager;
    }

    public static GCMRegisterTask getInstance(GCMManager gcmManager) {
        if (singletonThread==null || singletonThread.getSuccess()!=null) {
            singletonThread = new GCMRegisterTask(gcmManager);
        }
        return singletonThread;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Exception getResultException() {
        return resultException;
    }

    @Override
    public synchronized void start() {
        if (isAlive()) return;
        success = null;
        resultException = null;
        super.start();
    }

    @Override
    public void run() {
        try {
            doRegister();
            success = true;
        } catch (Exception e) {
            ExceptionUtil.ignoreException(e);
            success = false;
            resultException = e;
        }
    }

    private synchronized void doRegister() {
        int attempt = 0;
        boolean result = false;
        do {
            try {
                attempt++;

                try {
                    String regId = gcmManager.registerToGCMService();
                    Log.i(TAG, "Registration to GCM Service success. RegId = "+regId);
                    result = true;
                } catch (IOException e) {
                    result = false;
                }
                if (result) break;

                long delay = (long)Math.pow(2, attempt);
                Log.i(TAG, "El registro del dispositivo a GCM falló. Esperando " + delay + " segundos.");
                wait(delay * 1000);
            } catch (InterruptedException e) {
                ExceptionUtil.ignoreException(e);
            }
        } while (true);

        GCMSendRegIdToBackendTask gcmSendRegIdToBackendTask = GCMSendRegIdToBackendTask.getInstance(gcmManager);
        gcmSendRegIdToBackendTask.start();
    }
}
