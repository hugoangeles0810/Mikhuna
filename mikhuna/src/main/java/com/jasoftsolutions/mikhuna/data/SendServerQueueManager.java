package com.jasoftsolutions.mikhuna.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jasoftsolutions.mikhuna.model.SendServerQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc07 on 05/06/2014.
 */
public class SendServerQueueManager {

    private static final String TAG = ManagementManager.class.getSimpleName();

    public ArrayList<SendServerQueue> getSendServerQueueList(String selection, String[] args,
             String orderBy, String limit) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();

        ArrayList<SendServerQueue> result = new ArrayList<SendServerQueue>();

        Cursor cursor = db.query(Schema.SendServerQueue._tableName, new String[] {
                Schema.SendServerQueue.id, Schema.SendServerQueue.url, Schema.SendServerQueue.method,
                Schema.SendServerQueue.data, Schema.SendServerQueue.tag, Schema.SendServerQueue.priority,
                Schema.SendServerQueue.wifiOnly
        }, selection, args, null, null, orderBy, limit);

        if (cursor.moveToFirst()) {
            do {
                SendServerQueue ssq = new SendServerQueue();
                ssq.setId(cursor.getLong(0));
                ssq.setUrl(cursor.getString(1));
                ssq.setMethod(cursor.getString(2));
                ssq.setData(cursor.getString(3));
                ssq.setTag(cursor.getString(4));
                ssq.setPriority(cursor.getInt(5));
                ssq.setWifiOnly(cursor.getInt(6) > 0);

                result.add(ssq);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return result;
    }

    public void saveSendServerQueue(SendServerQueue sendServerQueue) {
        List<SendServerQueue> list = new ArrayList<SendServerQueue>();
        list.add(sendServerQueue);
        saveSendServerQueues(list);
    }

    public void saveSendServerQueues(List<SendServerQueue> list) {
        if (list == null || list.size() == 0) return;

        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();

        db.beginTransaction();
        try {
            for (SendServerQueue ssq : list) {
                ContentValues val = new ContentValues();

                val.put(Schema.SendServerQueue.url, ssq.getUrl());
                val.put(Schema.SendServerQueue.method, ssq.getMethod());
                val.put(Schema.SendServerQueue.data, ssq.getData());
                val.put(Schema.SendServerQueue.tag, ssq.getTag());
                val.put(Schema.SendServerQueue.priority, ssq.getPriority());
                val.put(Schema.SendServerQueue.wifiOnly, ssq.isWifiOnly());

                ssq.setId(DbUtil.saveRegister(db, Schema.SendServerQueue._tableName, val));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public SendServerQueue getNextSend(boolean hasWifi) {
        String selection = null;
        String args[] = null;
        if (!hasWifi) {
            selection = Schema.SendServerQueue.wifiOnly + " = ?";
            args = new String[] { "0" };
        }
        String orderBy = Schema.SendServerQueue.priority + " desc, "
                + Schema.SendServerQueue.id + " asc";
        ArrayList<SendServerQueue> list = getSendServerQueueList(selection, args, orderBy, "1");

        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public void removeById(long id) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();
        DbUtil.removeById(db, Schema.SendServerQueue._tableName, id);
    }
}
