package com.jasoftsolutions.mikhuna.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.jasoftsolutions.mikhuna.data.SendServerQueueManager;
import com.jasoftsolutions.mikhuna.model.SendServerQueue;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc07 on 23/06/2014.
 */
public class QueueSenderRemote {

    private static Map<Context, QueueSenderRemote> instances = new HashMap<Context, QueueSenderRemote>();

    private Context context;
    private SendServerQueueManager sendServerQueueManager;

    public static QueueSenderRemote getInstance(Context context) {
        Context appContext = context.getApplicationContext();
        QueueSenderRemote instance = instances.get(appContext);
        if (instance == null) {
            instance = new QueueSenderRemote(appContext);
            instances.put(appContext, instance);
        }
        return instance;
    }

    private QueueSenderRemote(Context context) {
        this.context = context;
        this.sendServerQueueManager = new SendServerQueueManager();
    }

    public long sendData(String url, HttpMethod method, List<NameValuePair> data,
                              boolean wifiOnly, int priority) {
        SendServerQueue sendServerQueue = new SendServerQueue();
        sendServerQueue.setUrl(url);
        sendServerQueue.setMethod(method.name());
        sendServerQueue.setData(serializeNameValuePairs(data));
        sendServerQueue.setPriority(priority);
        sendServerQueue.setWifiOnly(wifiOnly);
        sendServerQueueManager.saveSendServerQueue(sendServerQueue);

        return sendServerQueue.getId();
    }

    public static String serializeNameValuePairs(List<NameValuePair> data) {
        String[][] dataArray = new String[data.size()][2];
        Gson gson = new Gson();

        for (int i=0; i<data.size(); i++) {
            NameValuePair pair = data.get(i);
            dataArray[i][0] = pair.getName();
            dataArray[i][1] = pair.getValue();
        }

        return gson.toJson(dataArray);
    }

    public static List<NameValuePair> deserializeNameValuePairs(String data) {
        List<NameValuePair> result = new ArrayList<NameValuePair>();

        Gson gson = new Gson();
        String[][] srcData = gson.fromJson(data, String[][].class);

        for (String[] pairArray : srcData) {
            result.add(new BasicNameValuePair(pairArray[0], pairArray[1]));
        }

        return result;
    }

}
