package com.jasoftsolutions.mikhuna.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by gin on 27/04/14.
 */
public class DbUtil {

    public static Long getIdFromServerId(SQLiteDatabase db, String tableName, Long serverId) {
        Cursor cursor = db.query(tableName, new String[] {Schema.BaseTable.id},
                Schema.BaseTable.serverId+"=?", new String[]{serverId.toString()}, null, null, null);

        Long result=null;
        if (cursor.moveToFirst()) {
            result = cursor.getLong(0);
        }

        cursor.close();

        return result;
    }

    public static Long saveRegister(SQLiteDatabase db, String tableName, ContentValues values) {
        try {
            return db.insertOrThrow(tableName, null, values);
        } catch (SQLiteConstraintException e) {
            Long serverId = values.getAsLong(Schema.BaseTable.serverId);
            values.remove(Schema.BaseTable.serverId);
            db.update(tableName, values, Schema.BaseTable.serverId+"=?",
                    new String[] { serverId.toString() });
            return getIdFromServerId(db, tableName, serverId);
        }
    }

    public static int count(SQLiteDatabase db, String tableName, String where, String[] args) {
        Cursor cursor = db.query(tableName, new String[] { "count(1)" }, where, args,
                null, null, null);

        int result = 0;

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    public static void removeById(SQLiteDatabase db, String tableName, long id) {
        db.delete(tableName, Schema.BaseTable.id + " = ?", new String[] { String.valueOf(id) });
    }

    public static void removeByServerId(SQLiteDatabase db, String tableName, long serverId) {
        db.delete(tableName, Schema.BaseTable.serverId + " = ?",
                new String[] { String.valueOf(serverId) });
    }

    public static Boolean getCursorBoolean(Cursor cursor, int fieldPosition) {
        if (cursor.getInt(fieldPosition) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean existsByServerId(SQLiteDatabase db, String tableName, Long serverId) {
        if (serverId == null) {
            return false;
        }
        return count(db, tableName, Schema.BaseTable.serverId, new String[] {serverId.toString()}) > 0;
    }

    public static Boolean queryForBoolean(SQLiteDatabase db, String tableName, String fieldName, String where,
                           String[] whereArgs) {

        Cursor cursor = db.query(tableName, new String[] { fieldName }, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            Boolean result = getCursorBoolean(cursor, 0);
            cursor.close();
            return result;
        } else {
            return null;
        }
    }

    public static Boolean queryForBooleanByServerId(SQLiteDatabase db, String tableName,
                                                    String fieldName, Long serverId) {
        return queryForBoolean(db, tableName, fieldName, Schema.BaseTable.serverId+"=?",
                new String[] { serverId.toString() });
    }

    public static Long queryForLong(SQLiteDatabase db, String tableName, String fieldName, String where,
                                          String[] whereArgs) {

        Cursor cursor = db.query(tableName, new String[] { fieldName }, where, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            Long result = cursor.getLong(0);
            cursor.close();
            return result;
        } else {
            return null;
        }
    }

    public static Long queryForLongByServerId(SQLiteDatabase db, String tableName,
                                                    String fieldName, Long serverId) {
        return queryForLong(db, tableName, fieldName, Schema.BaseTable.serverId+"=?",
                new String[] { serverId.toString() });
    }
}
