package com.jasoftsolutions.mikhuna.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jasoftsolutions.mikhuna.model.Audit;
import com.jasoftsolutions.mikhuna.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc07 on 08/04/2014.
 */
public class AuditManager {

    private static final String TAG = AuditManager.class.getSimpleName();

    public void registerAudit(int actionId, Long restaurantServerId) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();

        long now = DateUtil.getCurrentTime();

        ContentValues val=new ContentValues();

        val.put(Schema.Audit.actionId, actionId);
        val.put(Schema.Audit.actionDate, now);
        val.put(Schema.Audit.restaurantServerId, restaurantServerId);
        val.put(Schema.Audit.sent, false);

        db.insert(Schema.Audit._tableName, null, val);

        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
    }

    /**
     * Devuelve todas las acciones del usuario que aún no han sido enviadas al servidor
     * @param limit Indica un límita para los datos. Puede ser nulo.
     * @return
     */
    public List<Audit> getPendingAudits(Integer limit) {
        SQLiteDatabase db=MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();

        Cursor cursor=db.query(Schema.Audit._tableName, new String[] {
            Schema.Audit.id, Schema.Audit.actionId, Schema.Audit.actionDate,
                Schema.Audit.restaurantServerId
        }, Schema.Audit.sent+"=?", new String[]{"0"}, null, null, Schema.Audit.actionDate, limit.toString());

        List<Audit> result=null;
        if (cursor.moveToFirst()) {
            result=new ArrayList<Audit>();
            do {
                Audit a=new Audit();
                a.setId(cursor.getLong(0));
                a.setActionId(cursor.getInt(1));
                a.setActionDate(cursor.getLong(2));
                if (!cursor.isNull(3)) a.setRestaurantServerId(cursor.getLong(3));

                result.add(a);
            } while (cursor.moveToNext());
        }

        cursor.close();
        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
        return result;
    }

    public void markSentTo(List<Audit> audits) {
        if (audits==null || audits.size()==0) return;

        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();

        for (Audit a : audits) {
//            ContentValues val=new ContentValues();
//            val.put(Schema.Audit.sent, true);
//            db.update(Schema.Audit._tableName, val, Schema.Audit.id+"=?",
//                    new String[] {a.getId().toString()});
            db.delete(Schema.Audit._tableName, Schema.Audit.id+"=?",
                    new String[] {a.getId().toString()});
        }

        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
    }
}
