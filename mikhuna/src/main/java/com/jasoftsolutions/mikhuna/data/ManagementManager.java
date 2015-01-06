package com.jasoftsolutions.mikhuna.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jasoftsolutions.mikhuna.domain.SelectOption;
import com.jasoftsolutions.mikhuna.domain.UbigeoCategory;
import com.jasoftsolutions.mikhuna.model.RestaurantCategory;
import com.jasoftsolutions.mikhuna.model.Ubigeo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc07 on 25/04/2014.
 */
public class ManagementManager {

    private static final String TAG = ManagementManager.class.getSimpleName();

    public void saveUbigeos(List<Ubigeo> ubigeos) {
        if (ubigeos==null || ubigeos.isEmpty()) return;

        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();

        db.beginTransaction();
        try {
            for (Ubigeo u : ubigeos) {
                ContentValues val=new ContentValues();

                val.put(Schema.Ubigeo.serverId, u.getServerId());
                if (u.getParentUbigeoServerId()!=null) {
                    val.put(Schema.Ubigeo.parentUbigeoServerId, u.getParentUbigeoServerId());
                } else if (u.getParentUbigeo()!=null) {
                    val.put(Schema.Ubigeo.parentUbigeoServerId, u.getParentUbigeo().getServerId());
                }
                val.put(Schema.Ubigeo.name, u.getName());
                val.put(Schema.Ubigeo.ubigeoCategoryId, u.getUbigeoCategoryId());

                u.setId(DbUtil.saveRegister(db, Schema.Ubigeo._tableName, val));
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    public void saveRestCategories(List<RestaurantCategory> categories) {
        if (categories==null || categories.isEmpty()) return;

        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();

        db.beginTransaction();
        try {
            for (RestaurantCategory c : categories) {
                ContentValues val=new ContentValues();

                val.put(Schema.RestaurantCategory.serverId, c.getServerId());
                val.put(Schema.RestaurantCategory.parentUbigeoServerId, c.getParentUbigeoServerId());
                val.put(Schema.RestaurantCategory.name, c.getName());

                c.setId(DbUtil.saveRegister(db, Schema.RestaurantCategory._tableName, val));
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<SelectOption> getSelectOptions(String tableName, String idField,
                                                    String nameField, String selection,
                                                    String... selectionArgs) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        assert db != null;

        ArrayList<SelectOption> selectOptionList = new ArrayList<SelectOption>();

        Cursor cursor = db.query(tableName, new String[] {
                idField, nameField
        }, selection, selectionArgs, null, null, nameField);

        if (cursor.moveToFirst()) {
            do {
                SelectOption um = new SelectOption();

                um.setId(cursor.getLong(0));
                um.setName(cursor.getString(1));

                selectOptionList.add(um);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return selectOptionList;
    }

    public ArrayList<Long> getIdsArray(String tableName, String idField,
                                                    String nameField,
                                                    String selection,
                                                    String... selectionArgs) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        assert db != null;

        ArrayList<Long> result = new ArrayList<Long>();

        Cursor cursor = db.query(tableName, new String[] {
                idField
        }, selection, selectionArgs, null, null, nameField);

        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getLong(0));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return result;
    }

    public ArrayList<SelectOption> getUbigeos(String selection, String... selectionArgs) {
        return getSelectOptions(Schema.Ubigeo._tableName, Schema.Ubigeo.serverId, Schema.Ubigeo.name,
                selection, selectionArgs);
    }

    public ArrayList<SelectOption> getUbigeosFrom(Long serverId) {
        return getUbigeos(Schema.Ubigeo.parentUbigeoServerId+"=? and " +
                Schema.Ubigeo.ubigeoCategoryId+"=?",
                serverId.toString(), String.valueOf(UbigeoCategory.LOCALITY.getId()));
    }

    public ArrayList<SelectOption> getRestaurantCategories(String selection, String... selectionArgs) {
        return getSelectOptions(Schema.RestaurantCategory._tableName, Schema.RestaurantCategory.serverId,
                Schema.RestaurantCategory.name, selection, selectionArgs);
    }

    public ArrayList<SelectOption> getAllRestaurantCategories() {
        return getRestaurantCategories(null, null);
    }

    public ArrayList<Long> getAllRestaurantCategoriesId() {
        return getIdsArray(Schema.RestaurantCategory._tableName, Schema.RestaurantCategory.serverId,
                Schema.RestaurantCategory.name, null, null);
    }

//    public ArrayList<SelectOption> getServiceTypes(String selection, String... selectionArgs) {
//        return getSelectOptions(Schema.ServiceType._tableName, Schema.ServiceType.id,
//                Schema.ServiceType.name, selection, selectionArgs);
//    }
//
//    public ArrayList<SelectOption> getAllServiceTypes() {
//        return getServiceTypes(null, null);
//    }

}
