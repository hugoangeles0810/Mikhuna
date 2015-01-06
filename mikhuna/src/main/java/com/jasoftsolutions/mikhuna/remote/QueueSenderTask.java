package com.jasoftsolutions.mikhuna.remote;

import android.content.Context;

import com.jasoftsolutions.mikhuna.data.SendServerQueueManager;
import com.jasoftsolutions.mikhuna.model.SendServerQueue;
import com.jasoftsolutions.mikhuna.remote.json.JsonResponse;
import com.jasoftsolutions.mikhuna.util.ContextUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc07 on 03/06/2014.
 */
public class QueueSenderTask extends Thread {

    private static final String THREAD_NAME = QueueSenderTask.class.getName();
    private static final String TAG = QueueSenderTask.class.getSimpleName();

    private static Map<Context, QueueSenderTask> instances = new HashMap<Context, QueueSenderTask>();

    private Context context;
    private SendServerQueueManager sendServerQueueManager;

    public static QueueSenderTask getInstance(Context context, SendServerQueueManager queueManager) {
        Context appContext = context.getApplicationContext();
        QueueSenderTask instance = instances.get(appContext);
        if (instance==null || !instance.isAlive()) {
            instance = new QueueSenderTask(appContext, queueManager);
            instances.put(appContext, instance);
        }
        return instance;
    }

    private QueueSenderTask(Context context, SendServerQueueManager sendServerQueueManager) {
        super(THREAD_NAME);
        this.context = context;
        this.sendServerQueueManager = sendServerQueueManager;
    }

    @Override
    public void run() {

        while (sendNext()) ;

    }

    /**
     *
     * @return Falso si ya no tiene nada por enviar. True en otro caso.
     */
    private boolean sendNext() {
        SendServerQueue data = sendServerQueueManager.getNextSend(
                ContextUtil.isWifiConnected(context));

        if (data == null) {
            return false;
        }

        sendData(data);

        return true;
    }

    private void sendData(SendServerQueue data) {
        RemoteHandler sh = new RemoteHandler();

        JsonResponse response = sh.requestResourceFromUrl(data.getUrl(),
                HttpMethod.valueOf(data.getMethod()),
                QueueSenderRemote.deserializeNameValuePairs(data.getData()),
                null, JsonResponse.class);

        if (response.getSuccess() != null && response.getSuccess()) {
            sendServerQueueManager.removeById(data.getId());
        }
    }
}
