package com.jasoftsolutions.mikhuna.remote;

import android.content.Context;

/**
 * Created by pc07 on 08/04/2014.
 */
public class AuditSenderTask extends Thread {

    private static final String THREAD_NAME = AuditSenderTask.class.getName();
    private static final String TAG = AuditSenderTask.class.getSimpleName();

    private static AuditSenderTask instance;

    private Context context;

    private AuditSenderTask(Context context) {
        this.context = context;
    }

    public static AuditSenderTask getInstance(Context context) {
        if (instance==null || !instance.isAlive()) {
            instance = new AuditSenderTask(context);
        }
        return instance;
    }

    public void startIfNecessary() {
        if (!isAlive()) start();
    }

    @Override
    public void run() {
        new AuditRemote(context).sendAudit();

        instance=null;
    }
}
