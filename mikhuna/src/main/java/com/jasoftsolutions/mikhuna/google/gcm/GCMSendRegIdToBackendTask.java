package com.jasoftsolutions.mikhuna.google.gcm;

import android.util.Log;

import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

import java.io.IOException;

/**
 * Created by pc07 on 09/04/2014.
 */
public class GCMSendRegIdToBackendTask extends Thread {

    private static final String TAG = GCMSendRegIdToBackendTask.class.getSimpleName();

    private static GCMSendRegIdToBackendTask singletonThread;

    private Boolean success;
    private Exception resultException;

    private GCMManager gcmManager;

    private GCMSendRegIdToBackendTask(GCMManager gcmManager) {
        this.gcmManager = gcmManager;
    }

    public static GCMSendRegIdToBackendTask getInstance(GCMManager gcmManager) {
        if (singletonThread==null || singletonThread.getSuccess()!=null) {
            singletonThread = new GCMSendRegIdToBackendTask(gcmManager);
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
            doTask();
            success = true;
        } catch (IOException e) {
            success = false;
            resultException = e;
            ExceptionUtil.handleException(e);
        }
    }

    private synchronized void doTask() throws IOException {
        int attempt = 0;
        boolean result = false;
        do {
            try {
                attempt++;

                result = gcmManager.sendRegIdToBackend();
                if (result) break;

                long delay = (long)Math.pow(2, attempt);
                Log.i(TAG, "El envío de Id de registro al backend falló. Esperando " + delay + " segundos.");
                wait(delay * 1000);
            } catch (InterruptedException e) {
                ExceptionUtil.ignoreException(e);
            }
        } while (true);

        Log.i(TAG, "GCM Reg Id sent to backend successfully.");
    }


}
