package com.jasoftsolutions.mikhuna.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jasoftsolutions.mikhuna.domain.RestaurantListFilter;
import com.jasoftsolutions.mikhuna.domain.Weekday;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.RestaurantCategoryAssign;
import com.jasoftsolutions.mikhuna.model.RestaurantDish;
import com.jasoftsolutions.mikhuna.model.RestaurantDishCategory;
import com.jasoftsolutions.mikhuna.model.RestaurantDishPresentation;
import com.jasoftsolutions.mikhuna.model.RestaurantPromotion;
import com.jasoftsolutions.mikhuna.model.RestaurantTimetable;
import com.jasoftsolutions.mikhuna.util.ArrayUtil;
import com.jasoftsolutions.mikhuna.util.DateUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc07 on 02/04/2014.
 */
public class RestaurantManager {

//    /**
//     * Instancia singleton
//     */
//    private static RestaurantManager instance;
//
//    private RestaurantManager() {}
//
//    public static RestaurantManager getInstance() {
//        return instance;
//    }

    private static final String TAG = RestaurantManager.class.getSimpleName();

    private Cursor defaultRestaurantQuery(SQLiteDatabase db, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return db.query(Schema.Restaurant._tableName, new String[]{
                Schema.Restaurant.id, Schema.Restaurant.name, Schema.Restaurant.description,
                Schema.Restaurant.phoneNumber, Schema.Restaurant.currency, Schema.Restaurant.minAmount,
                Schema.Restaurant.rating, Schema.Restaurant.imageId, Schema.Restaurant.minDeliveryTime,
                Schema.Restaurant.maxDeliveryTime, Schema.Restaurant.latitude, Schema.Restaurant.longitude,
                Schema.Restaurant.address, Schema.Restaurant.shippingCost, Schema.Restaurant.serverId,
                Schema.Restaurant.lastUpdate, Schema.Restaurant.logoUrl, Schema.Restaurant.smallLogoUrl,
                Schema.Restaurant.weight, Schema.Restaurant.serviceTypeId, Schema.Restaurant.ubigeoServerId,
                Schema.Restaurant.timetableDescription,
                Schema.Restaurant.numberProductCategory,
                Schema.Restaurant.categoryLastUpdate
        }, selection, selectionArgs, groupBy, having, orderBy);
    }

    private Restaurant mapFromCursor(Cursor cursor) {
        Restaurant r=new Restaurant();
        r.setId(cursor.getLong(0));
        r.setName(cursor.getString(1));
        r.setDescription(cursor.getString(2));
        r.setPhoneNumber(cursor.getString(3));
        r.setCurrency(cursor.getString(4));
        r.setMinAmount(cursor.getFloat(5));
        r.setRating(cursor.getFloat(6));
        r.setImageId(cursor.getInt(7));
        r.setMinDeliveryTime(cursor.getInt(8));
        r.setMaxDeliveryTime(cursor.getInt(9));
        r.setLatitude(cursor.getDouble(10));
        r.setLongitude(cursor.getDouble(11));
        r.setAddress(cursor.getString(12));
        r.setShippingCost(cursor.getFloat(13));
        r.setServerId(cursor.getLong(14));
        r.setLastUpdate(cursor.getLong(15));
        r.setLogoUrl(cursor.getString(16));
        r.setSmallLogoUrl(cursor.getString(17));
        r.setWeight(cursor.getInt(18));
        r.setServiceTypeId(cursor.getInt(19));
        if (!cursor.isNull(20)) r.setUbigeoServerId(cursor.getLong(20));
        r.setTimetableDescription(cursor.getString(21));
        r.setNumberProductCategory(cursor.getInt(22));
        r.setCategoryLastUpdate(cursor.getLong(23));
//        if (!cursor.isNull(1)) r.setName(cursor.getString(1));
//        if (!cursor.isNull(2)) r.setDescription(cursor.getString(2));
//        if (!cursor.isNull(3)) r.setPhoneNumber(cursor.getString(3));
//        if (!cursor.isNull(4)) r.setCurrency(cursor.getString(4));
//        if (!cursor.isNull(5)) r.setMinAmount(cursor.getFloat(5));
//        if (!cursor.isNull(6)) r.setRating(cursor.getFloat(6));
//        if (!cursor.isNull(7)) r.setImageId(cursor.getInt(7));
//        if (!cursor.isNull(8)) r.setMinDeliveryTime(cursor.getInt(8));
//        if (!cursor.isNull(9)) r.setMaxDeliveryTime(cursor.getInt(9));
//        if (!cursor.isNull(10)) r.setLatitude(cursor.getDouble(10));
//        if (!cursor.isNull(11)) r.setLongitude(cursor.getDouble(11));
//        if (!cursor.isNull(12)) r.setAddress(cursor.getString(12));
//        if (!cursor.isNull(13)) r.setShippingCost(cursor.getFloat(13));
//        if (!cursor.isNull(14)) r.setServerId(cursor.getLong(14));
//        if (!cursor.isNull(15)) r.setLastUpdate(cursor.getLong(15));
//        if (!cursor.isNull(16)) r.setLogoUrl(cursor.getString(16));
//        if (!cursor.isNull(17)) r.setSmallLogoUrl(cursor.getString(17));
//        if (!cursor.isNull(18)) r.setWeight(cursor.getInt(18));

        return r;
    }

    public List<Restaurant> getRestaurantList() {
        return getRestaurantList(null, null);
    }

    /**
     * Retorna un array con lo necesario para realizar los filtros a los restaurantes en el siguiente orden:
     * (String)selection, (String[])argArray, (String)restaurantCategories
     * @param name
     * @param filter
     * @return
     */
    private Object[] processFilter(String name, RestaurantListFilter filter) {
        ArrayList<String> selection = new ArrayList<String>();
        ArrayList<String> args = new ArrayList<String>();
        String restaurantCategories = null;

        if (name != null && name.trim().length() > 0) {
            selection.add(Schema.Restaurant.name + " like ?");
            args.add("%"+name+"%");
        }

        if (filter != null) {
            if (filter.getUbigeoServerId() != null) {
                selection.add(Schema.Restaurant.ubigeoServerId + " = ?");
                args.add(filter.getUbigeoServerId().toString());
            }
            if (filter.getServiceTypes() != null) {
                int sum = 0;
                for (long st : filter.getServiceTypes()) {
                    sum |= st;
                }
                selection.add(Schema.Restaurant.serviceTypeId + " & " + sum + " > 0");
            }
            if (filter.getRestaurantCategories() != null) {
                restaurantCategories = ArrayUtil.implode(filter.getRestaurantCategories(), ",");
            }
        }

        String[] argArray = new String[args.size()];
        for (int i = 0; i < args.size(); i++) {
            argArray[i] = args.get(i);
        }

        return new Object[] {
                ArrayUtil.implode(selection, " and "),
                argArray,
                restaurantCategories
        };
    }

    public ArrayList<Long> queryRestaurantServerIds(String name, RestaurantListFilter filter) {
        Object[] resultFilter = processFilter(name, filter);

        String selection = (String)resultFilter[0];
        String[] argArray = (String[])resultFilter[1];
        String restaurantCategories = (String)resultFilter[2];

        ArrayList<Long> result = getRestaurantServerIds(selection, argArray);

        if (restaurantCategories != null) {
            result = filterRestaurantServerIdsByCategoriesIn(result, restaurantCategories);
        }

        return result;
    }

    public List<Restaurant> queryRestaurantList(String name, RestaurantListFilter filter) {
        Object[] resultFilter = processFilter(name, filter);

        String selection = (String)resultFilter[0];
        String[] argArray = (String[])resultFilter[1];
        String restaurantCategories = (String)resultFilter[2];

        List<Restaurant> result = getRestaurantList(selection, argArray);

        if (restaurantCategories != null) {
            result = filterRestaurantByCategoriesIn(result, restaurantCategories);
        }

        return result;
    }

    /**
     *
     * @param selection
     * @param selectionArgs
     * @param deeper Indica si se deben cargar los datos más detallados como por ejemplo las
     *               categorías de sus platillos
     * @return
     */
    public List<Restaurant> getRestaurantList(String selection, String[] selectionArgs, boolean deeper) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        assert db != null;
        Cursor cursor = defaultRestaurantQuery(db, selection, selectionArgs, null, null,
                Schema.Restaurant.weight + " desc, " + Schema.Restaurant.rating + " desc");

        List<Restaurant> result=new ArrayList<Restaurant>();
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                Restaurant r=mapFromCursor(cursor);
                result.add(r);
            } while (cursor.moveToNext());
        }
        loadRestaurantTimetable(result, db);
        if (deeper) {
//            loadRestaurantDishCategories(result, db);
        }

        cursor.close();
        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
        return getRestaurantsOrderedByIsOpen(result);
    }

    public List<Restaurant> getRestaurantList(String selection, String[] selectionArgs) {
        return getRestaurantList(selection, selectionArgs, false);
    }

    private List<Restaurant> getRestaurantsOrderedByIsOpen(List<Restaurant> restaurantList) {
        try {
            List<Restaurant> openList = new ArrayList<Restaurant>();
            List<Restaurant> closedList = new ArrayList<Restaurant>();
            List<Restaurant> unespecifiedList = new ArrayList<Restaurant>();

            for (Restaurant r : restaurantList) {
                if (r.isOpen() == null) {
                    unespecifiedList.add(r);
                } else if (r.isOpen()) {
                    openList.add(r);
                } else {
                    closedList.add(r);
                }
            }

            List<Restaurant> result = new ArrayList<Restaurant>();
            result.addAll(openList);
            result.addAll(closedList);
            result.addAll(unespecifiedList);

            return result;
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
            return restaurantList;
        }
    }

    public ArrayList<Long> getRestaurantServerIds(String selection, String[] selectionArgs) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        assert db != null;

        Cursor cursor = db.query(Schema.Restaurant._tableName, new String[]{
                Schema.Restaurant.serverId
        }, selection, selectionArgs, null, null, Schema.Restaurant.weight + " desc, " + Schema.Restaurant.rating + " desc");

        ArrayList<Long> result=new ArrayList<Long>();
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                result.add(cursor.getLong(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
        return result;
    }

    public Restaurant getRestaurantById(Long id) {
        List<Restaurant> result=getRestaurantList(Schema.Restaurant.id + " = ?",
                new String[] {id.toString()});

        if (result!=null && result.size()==1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public Restaurant getRestaurantByServerId(Long serverId) {
        List<Restaurant> result=getRestaurantList(Schema.Restaurant.serverId + " = ?",
                new String[] {serverId.toString()}, true);

        if (result!=null && result.size()==1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public void loadRestaurantTimetable(List<Restaurant> restaurantList, SQLiteDatabase db) {
        if (restaurantList == null || restaurantList.size()==0) return;

        Map<Long, Restaurant> restaurantMap=new HashMap<Long, Restaurant>();
        String inStmt = "";
        for (Restaurant r : restaurantList) {
            // Recién se cargarán los horarios (aún no se sabe si está abierto o no)
            r.setOpen(null);

            restaurantMap.put(r.getId(), r);
            if (r.getRestaurantTimetables()==null) {
                r.setRestaurantTimetables(new ArrayList<RestaurantTimetable>());
            }
            inStmt += ", "+ r.getId();
        }
        if (inStmt.length()>0) {
            inStmt = inStmt.substring(2);
        }

        Cursor cursor=db.query(Schema.RestaurantTimeTable._tableName, new String[] {
                Schema.RestaurantTimeTable.id, Schema.RestaurantTimeTable.restaurantId,
                Schema.RestaurantTimeTable.weekday, Schema.RestaurantTimeTable.startTime,
                Schema.RestaurantTimeTable.finishTime, Schema.RestaurantTimeTable.serverId
        }, Schema.RestaurantTimeTable.restaurantId + " in ("+inStmt+")", null, null, null,
            Schema.RestaurantTimeTable.weekday + ", " + Schema.RestaurantTimeTable.startTime
        );

        if (cursor.moveToFirst()) {
            do {
                RestaurantTimetable rt=new RestaurantTimetable();
                rt.setId(cursor.getLong(0));

                Long restaurantId = cursor.getLong(1);
                Restaurant r = restaurantMap.get(restaurantId);
                rt.setRestaurant(r);

                rt.setWeekday(cursor.getInt(2));
                rt.setStartTime(cursor.getString(3));
                rt.setFinishTime(cursor.getString(4));

                rt.setServerId(cursor.getLong(5));

                if (r.isOpen() == null || r.isOpen()!=true) {
                    // r.setOpen(verifyIsOpen(rt));
                    setRestaurantOpenOrClosed(r, rt);
                }

                r.getRestaurantTimetables().add(rt);
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    /**
     *
     * @param restaurantServerId
     * @return
     */
    public ArrayList<RestaurantDishCategory> getRestaurantDishCategoriesOf(long restaurantServerId) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();

        Cursor cursor = db.query(Schema.RestaurantDishCategory._tableName, new String[] {
                        Schema.RestaurantDishCategory.id, Schema.RestaurantDishCategory.serverId,
                        Schema.RestaurantDishCategory.restaurantServerId, Schema.RestaurantDishCategory.name,
                        Schema.RestaurantDishCategory.position, Schema.RestaurantDishCategory.dishesLastUpdate
                }, Schema.RestaurantDishCategory.restaurantServerId + "=?",
                new String[] { String.valueOf(restaurantServerId) }, null, null,
                Schema.RestaurantDishCategory.position + ", " + Schema.RestaurantDishCategory.name
        );

        ArrayList<RestaurantDishCategory> result = new ArrayList<RestaurantDishCategory>();

        if (cursor.moveToFirst()) {
            do {
                RestaurantDishCategory rdc=new RestaurantDishCategory();
                rdc.setId(cursor.getLong(0));
                rdc.setServerId(cursor.getLong(1));
                rdc.setRestaurantServerId(cursor.getLong(2));
                rdc.setName(cursor.getString(3));
                rdc.setPosition(cursor.getInt(4));
                rdc.setDishesLastUpdate(cursor.getLong(5));

                result.add(rdc);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return result;
    }

    public ArrayList<RestaurantDish> getRestaurantDishesOf(long dishCategoryServerId) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.query(Schema.RestaurantDish._tableName, new String[] {
                        Schema.RestaurantDish.id, Schema.RestaurantDish.serverId,
                        Schema.RestaurantDish.restaurantDishCategoryServerId, Schema.RestaurantDish.name,
                        Schema.RestaurantDish.description, Schema.RestaurantDish.position,
                        Schema.RestaurantDish.price, Schema.RestaurantDish.liked,
                        Schema.RestaurantDish.likeCount
                }, Schema.RestaurantDish.restaurantDishCategoryServerId + "=?",
                new String[] { String.valueOf(dishCategoryServerId) }, null, null,
                Schema.RestaurantDish.position + ", " + Schema.RestaurantDish.name
        );

        ArrayList<RestaurantDish> result = new ArrayList<RestaurantDish>();

        if (cursor.moveToFirst()) {
            do {
                RestaurantDish rd=new RestaurantDish();
                rd.setId(cursor.getLong(0));
                rd.setServerId(cursor.getLong(1));
                rd.setDishCategoryServerId(cursor.getLong(2));
                rd.setName(cursor.getString(3));
                rd.setDescription(cursor.getString(4));
                rd.setPosition(cursor.getInt(5));
                rd.setPrice(cursor.getDouble(6));
                rd.setLiked(DbUtil.getCursorBoolean(cursor, 7));
                rd.setLikeCount(cursor.getLong(8));

                result.add(rd);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return result;
    }

    public ArrayList<RestaurantDishPresentation> getDishPresentationsOf(long dishServerId){
        ArrayList<RestaurantDishPresentation> presentations;
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        Cursor cursor = db.query(Schema.RestaurantDishPresentation._tableName, new String[]{
                    Schema.RestaurantDishPresentation.id, Schema.RestaurantDishPresentation.serverId,
                    Schema.RestaurantDishPresentation.restaurantDishServerId, Schema.RestaurantDishPresentation.name,
                    Schema.RestaurantDishPresentation.cost, Schema.RestaurantDishPresentation.position
            }, Schema.RestaurantDishPresentation.restaurantDishServerId + "=?",
            new String[]{ String.valueOf(dishServerId) }, null, null,
                Schema.RestaurantDishPresentation.position + ", " + Schema.RestaurantDishPresentation.name
        );

        presentations = new ArrayList<RestaurantDishPresentation>();

        if (cursor.moveToFirst()){
            do {
                RestaurantDishPresentation p = new RestaurantDishPresentation();
                p.setId(cursor.getLong(0));
                p.setServerId(cursor.getLong(1));
                p.setRestaurantDishServerId(cursor.getLong(2));
                p.setName(cursor.getString(3));
                p.setCost(cursor.getDouble(4));
                p.setPosition(cursor.getInt(5));
                presentations.add(p);
            }while(cursor.moveToNext());
        }
        
        return presentations;
    }

    public void loadRestaurantDishCategories(Restaurant restaurant, SQLiteDatabase db) {
        ArrayList<Restaurant> tmp = new ArrayList<Restaurant>();
        tmp.add(restaurant);
        loadRestaurantDishCategories(tmp, db);
    }

    public void loadRestaurantDishCategories(List<Restaurant> restaurantList, SQLiteDatabase db) {
        if (restaurantList == null || restaurantList.size()==0) return;

        Map<Long, Restaurant> restaurantMap=new HashMap<Long, Restaurant>();
        String inStmt = "";
        for (Restaurant r : restaurantList) {
            restaurantMap.put(r.getServerId(), r);
            if (r.getRestaurantDishCategories()==null) {
                r.setRestaurantDishCategories(new ArrayList<RestaurantDishCategory>());
            }
            inStmt += ", "+ r.getServerId();
        }
        if (inStmt.length()>0) {
            inStmt = inStmt.substring(2);
        }

        Cursor cursor=db.query(Schema.RestaurantDishCategory._tableName, new String[] {
                        Schema.RestaurantDishCategory.id, Schema.RestaurantDishCategory.serverId,
                        Schema.RestaurantDishCategory.restaurantServerId, Schema.RestaurantDishCategory.name,
                        Schema.RestaurantDishCategory.position, Schema.RestaurantDishCategory.dishesLastUpdate
                }, Schema.RestaurantDishCategory.restaurantServerId + " in ("+inStmt+")", null, null, null,
                Schema.RestaurantDishCategory.position + ", " + Schema.RestaurantDishCategory.name
        );

        if (cursor.moveToFirst()) {
            do {
                RestaurantDishCategory rdc=new RestaurantDishCategory();
                rdc.setId(cursor.getLong(0));
                rdc.setServerId(cursor.getLong(1));

                Long restaurantServerId = cursor.getLong(2);
                Restaurant r = restaurantMap.get(restaurantServerId);

                rdc.setName(cursor.getString(3));
                rdc.setPosition(cursor.getInt(4));
                rdc.setDishesLastUpdate(cursor.getLong(5));

                r.getRestaurantDishCategories().add(rdc);
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    /**
     *
     * @param restaurants
     * @param inString  Categorías separadas por comas (irán en una instrucción in)
     */
    public ArrayList<Restaurant> filterRestaurantByCategoriesIn(List<Restaurant> restaurants, String inString) {
        ArrayList<Restaurant> result = new ArrayList<Restaurant>();

        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();

        for (Restaurant r : restaurants) {
            Cursor cursor = db.query(Schema.RestaurantCategoryAssign._tableName,
                    new String[] { "count(1)" }, Schema.RestaurantCategoryAssign.restaurantId + " = ?" +
                            " and " + Schema.RestaurantCategoryAssign.restocatServerId + " in (" + inString + ")",
                    new String[] { r.getId().toString() }, null, null, null
            );

            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result.add(r);
                }
            }
            cursor.close();
        }

        return result;
    }

    /**
     *
     * @param restaurantIds
     * @param inString  Categorías separadas por comas (irán en una instrucción in)
     */
    public ArrayList<Long> filterRestaurantServerIdsByCategoriesIn(List<Long> restaurantIds, String inString) {
        ArrayList<Long> result = new ArrayList<Long>();

        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();

        String query =
                "select count(1) " +
                        "from " + Schema.RestaurantCategoryAssign._tableName + " rc " +
                            "inner join " + Schema.Restaurant._tableName + " r " +
                                "on r." + Schema.Restaurant.id + " = rc." + Schema.RestaurantCategoryAssign.restaurantId +
                        " where r." + Schema.Restaurant.serverId + " = ? " +
                            "and rc." + Schema.RestaurantCategoryAssign.restocatServerId + " in (" + inString + ")";

        for (Long rid : restaurantIds) {
            if (rid == null) continue;

            Cursor cursor = db.rawQuery(query, new String[] { rid.toString() });

            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result.add(rid);
                }
            }
            cursor.close();
        }

        return result;
    }

    public void saveRestaurant(Restaurant restaurant) {
        List<Restaurant> rl=new ArrayList<Restaurant>();
        rl.add(restaurant);
        saveRestaurants(rl);
    }

    public Long getRestaurantIdFromServerId(Long serverId) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        return getRestaurantIdFromServerId(db, serverId);
    }

    private Long getRestaurantIdFromServerId(SQLiteDatabase db, Long serverId) {
        Cursor cursor = db.query(Schema.Restaurant._tableName, new String[] {Schema.Restaurant.id},
                Schema.Restaurant.serverId+"=?", new String[]{serverId.toString()}, null, null, null);


        Long result=null;
        if (cursor.moveToFirst()) {
            result = cursor.getLong(0);
        }

        cursor.close();

        return result;
    }

    /**
     * Inserta o actualiza los restaurantes a la base de datos.
     * Los restaurantes se diferencian por su <code>serverId</code>
     * @param restaurantList
     */
    public void saveRestaurants(List<Restaurant> restaurantList) {
        if (restaurantList==null || restaurantList.size()==0) { return; }

        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();
        db.beginTransaction();

        try {
            for (Restaurant r : restaurantList) {
                ContentValues val=new ContentValues();
    //            val.put(Schema.Restaurant.id, r.getId());
                val.put(Schema.Restaurant.serverId, r.getServerId());
                val.put(Schema.Restaurant.name, r.getName());
                val.put(Schema.Restaurant.description, r.getDescription());
                val.put(Schema.Restaurant.phoneNumber, r.getPhoneNumber());
                val.put(Schema.Restaurant.currency, r.getCurrency());
                val.put(Schema.Restaurant.minAmount, r.getMinAmount());
                val.put(Schema.Restaurant.rating, r.getRating());
                val.put(Schema.Restaurant.imageId, r.getImageId());
                val.put(Schema.Restaurant.minDeliveryTime, r.getMinDeliveryTime());
                val.put(Schema.Restaurant.maxDeliveryTime, r.getMaxDeliveryTime());
                val.put(Schema.Restaurant.latitude, r.getLatitude());
                val.put(Schema.Restaurant.longitude, r.getLongitude());
                val.put(Schema.Restaurant.address, r.getAddress());
                val.put(Schema.Restaurant.shippingCost, r.getShippingCost());
                val.put(Schema.Restaurant.lastUpdate, r.getLastUpdate());
                val.put(Schema.Restaurant.logoUrl, r.getLogoUrl());
                val.put(Schema.Restaurant.smallLogoUrl, r.getSmallLogoUrl());
                val.put(Schema.Restaurant.weight, r.getWeight());
                val.put(Schema.Restaurant.serviceTypeId, r.getServiceTypeId());
                val.put(Schema.Restaurant.ubigeoServerId, r.getUbigeoServerId());
                val.put(Schema.Restaurant.timetableDescription, r.getTimetableDescription());
                val.put(Schema.Restaurant.numberProductCategory, r.getNumberProductCategory());

                try {
                    r.setId(db.insertOrThrow(Schema.Restaurant._tableName, null, val));
                } catch (SQLiteConstraintException e) {
                    ExceptionUtil.handleException(e);
                    val.remove(Schema.Restaurant.serverId);
                    db.update(Schema.Restaurant._tableName, val, Schema.Restaurant.serverId + "=?",
                            new String[]{r.getServerId().toString()});
                    r.setId(getRestaurantIdFromServerId(db, r.getServerId()));
                }

                if (r.getRestaurantTimetables()!=null) {
                    for (RestaurantTimetable rt : r.getRestaurantTimetables()) {
                        rt.setRestaurant(r);
                    }
                    cleanRestaurantTimetablesById(db, r.getId());
                    saveRestaurantTimetables(r.getRestaurantTimetables(), db);
                }

                if (r.getRestaurantCategoryAssigns()!=null && r.getRestaurantCategoryAssigns().size()>0) {
                    for (RestaurantCategoryAssign rca : r.getRestaurantCategoryAssigns()) {
                        rca.setRestaurant(r);
                    }

                    saveRestaurantCategories(r.getRestaurantCategoryAssigns(), db);
                }

                if (r.getRestaurantDishCategories() != null) {
                    cleanRestaurantDishesCategoriesByRestaurantServerId(db, r.getServerId());
                    saveRestaurantDishCategories(r.getRestaurantDishCategories(), db);
                }
            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
    }

    public void saveRestaurantTimetables(List<RestaurantTimetable> restaurantTimetableList, SQLiteDatabase db) {
        if (restaurantTimetableList==null || restaurantTimetableList.size()==0) { return; }

        for (RestaurantTimetable rt : restaurantTimetableList) {
            ContentValues val=new ContentValues();

            val.put(Schema.RestaurantTimeTable.serverId, rt.getServerId());
            val.put(Schema.RestaurantTimeTable.restaurantId, rt.getRestaurant().getId());
            val.put(Schema.RestaurantTimeTable.weekday, rt.getWeekday());
            val.put(Schema.RestaurantTimeTable.startTime, rt.getStartTime());
            val.put(Schema.RestaurantTimeTable.finishTime, rt.getFinishTime());

            try {
                DbUtil.removeByServerId(db, Schema.RestaurantTimeTable._tableName, rt.getServerId());
                db.insertOrThrow(Schema.RestaurantTimeTable._tableName, null, val);
            } catch (SQLiteConstraintException e) {
                val.remove(Schema.RestaurantTimeTable.serverId);
                db.update(Schema.RestaurantTimeTable._tableName, val, Schema.RestaurantTimeTable.serverId+"=?",
                        new String[] {rt.getServerId().toString()});
            }
        }
    }

    public void cleanRestaurantTimetablesById(SQLiteDatabase db, long restaurantId) {
        db.delete(Schema.RestaurantTimeTable._tableName, Schema.RestaurantTimeTable.restaurantId + "=?",
                new String[] {String.valueOf(restaurantId)} );
    }

    public void saveRestaurantDishCategories(List<RestaurantDishCategory> restaurantDishCategoryList, SQLiteDatabase db) {
        if (restaurantDishCategoryList==null) { return; }

        for (RestaurantDishCategory rdc : restaurantDishCategoryList) {
            ContentValues val=new ContentValues();

            val.put(Schema.RestaurantDishCategory.serverId, rdc.getServerId());
            val.put(Schema.RestaurantDishCategory.restaurantServerId, rdc.getRestaurantServerId());
            val.put(Schema.RestaurantDishCategory.name, rdc.getName());
            val.put(Schema.RestaurantDishCategory.position, rdc.getPosition());
            val.put(Schema.RestaurantDishCategory.dishesLastUpdate, rdc.getDishesLastUpdate());

            try {
                db.insertOrThrow(Schema.RestaurantDishCategory._tableName, null, val);
            } catch (SQLiteConstraintException e) {
                val.remove(Schema.RestaurantDishCategory.serverId);
                db.update(Schema.RestaurantDishCategory._tableName, val, Schema.RestaurantDishCategory.serverId+"=?",
                        new String[] { rdc.getServerId().toString() });
            }

            if(rdc.getRestaurantDishes()!=null){
                saveRestaurantDishes(rdc.getRestaurantDishes(), db);
            }

        }

    }

    public void saveRestaurantDishCategories(List<RestaurantDishCategory> restaurantDishCategoryList){
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();
        saveRestaurantDishCategories(restaurantDishCategoryList, db);
        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
    }

    public void cleanRestaurantDishesCategoriesByRestaurantServerId(SQLiteDatabase db,
                                                                    Long restaurantServerId) {
        db.delete(Schema.RestaurantDishCategory._tableName,
                Schema.RestaurantDishCategory.restaurantServerId + "=?",
                new String[] { String.valueOf(restaurantServerId) } );
    }

    public void saveRestaurantDishes(List<RestaurantDish> restaurantDishList, SQLiteDatabase db) {
//        if (restaurantDishList==null) { return; }

        for (RestaurantDish rd : restaurantDishList) {
            ContentValues val=new ContentValues();

            val.put(Schema.RestaurantDish.serverId, rd.getServerId());
            val.put(Schema.RestaurantDish.restaurantServerId, rd.getRestaurantServerId());
            val.put(Schema.RestaurantDish.restaurantDishCategoryServerId, rd.getDishCategoryServerId());
            val.put(Schema.RestaurantDish.name, rd.getName());
            val.put(Schema.RestaurantDish.description, rd.getDescription());
            val.put(Schema.RestaurantDish.position, rd.getPosition());
            val.put(Schema.RestaurantDish.price, rd.getPrice());
            val.put(Schema.RestaurantDish.liked, rd.getLiked());
            val.put(Schema.RestaurantDish.likeCount, rd.getLikeCount());

            try {
                db.insertOrThrow(Schema.RestaurantDish._tableName, null, val);
            } catch (Exception e) {
                val.remove(Schema.RestaurantDish.serverId);
                db.update(Schema.RestaurantDish._tableName, val, Schema.RestaurantDish.serverId+"=?",
                        new String[] { rd.getServerId().toString() });
            }

            if (rd.getDishPresentations()!=null){
                saveRestaurantDishPresentations(rd.getDishPresentations(), db);
            }
        }
    }

    public void updateRestaurantCategoryLastUpdate(Long restaurantServerId, Long lastUpdate){
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(Schema.Restaurant.categoryLastUpdate, lastUpdate);
        try {
            db.update(Schema.Restaurant._tableName, val, Schema.Restaurant.serverId+"=?",
                    new String[]{restaurantServerId.toString()});
        }catch(Exception e){
        }
    }

    private void saveRestaurantDishPresentations(List<RestaurantDishPresentation> dishPresentations, SQLiteDatabase db) {
        for (RestaurantDishPresentation dp : dishPresentations){
            ContentValues val = new ContentValues();
            val.put(Schema.RestaurantDishPresentation.serverId, dp.getServerId());
            val.put(Schema.RestaurantDishPresentation.name, dp.getName());
            val.put(Schema.RestaurantDishPresentation.position, dp.getPosition());
            val.put(Schema.RestaurantDishPresentation.cost, dp.getCost());
            val.put(Schema.RestaurantDishPresentation.restaurantDishServerId, dp.getRestaurantDishServerId());

            try {
                    db.insertOrThrow(Schema.RestaurantDishPresentation._tableName, null, val);
            }catch(Exception e){
                val.remove(Schema.RestaurantDishPresentation.serverId);
                db.update(Schema.RestaurantDishPresentation._tableName, val, Schema.RestaurantDishPresentation.serverId+"=?",
                        new String[] { dp.getServerId().toString() });
            }
        }
    }

    public void saveRestaurantCategories(List<RestaurantCategoryAssign> restaurantCategoryAssigns, SQLiteDatabase db) {
        if (restaurantCategoryAssigns==null || restaurantCategoryAssigns.size()==0) { return; }

        for (RestaurantCategoryAssign rca : restaurantCategoryAssigns) {
            ContentValues val=new ContentValues();

            val.put(Schema.RestaurantCategoryAssign.serverId, rca.getServerId());
            val.put(Schema.RestaurantCategoryAssign.restaurantId, rca.getRestaurant().getId());
            val.put(Schema.RestaurantCategoryAssign.restocatServerId, rca.getRestaurantCategoryServerId());

            rca.setId(DbUtil.saveRegister(db, Schema.RestaurantCategoryAssign._tableName, val));
        }
    }

    public int countRestaurantPromotions(String selection, String[] args) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        assert db != null;
        Cursor cursor = db.query(Schema.RestaurantPromotion._tableName,
                new String[] { "count(1)" }, selection, args, null, null, null);

        int result = 0;
        if (cursor.moveToFirst()) {
            result = cursor.getInt(0);
        }

        cursor.close();
        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
        return result;
    }

    public ArrayList<RestaurantPromotion> getRestaurantPromotions(String selection, String[] args) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        assert db != null;
        Cursor cursor = db.query(Schema.RestaurantPromotion._tableName, new String[] {
                Schema.RestaurantPromotion.id, Schema.RestaurantPromotion.serverId,
                Schema.RestaurantPromotion.restaurantServerId, Schema.RestaurantPromotion.title,
                Schema.RestaurantPromotion.startDate, Schema.RestaurantPromotion.finishDate,
                Schema.RestaurantPromotion.description, Schema.RestaurantPromotion.weight
            }, selection, args, null, null, Schema.RestaurantPromotion.weight + " desc, " +
                Schema.RestaurantPromotion.finishDate + " asc");

        ArrayList<RestaurantPromotion> result=new ArrayList<RestaurantPromotion>();
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                RestaurantPromotion rp = new RestaurantPromotion();

                rp.setId(cursor.getLong(0));
                rp.setServerId(cursor.getLong(1));

//                Restaurant r = new Restaurant();
//                r.setServerId(cursor.getLong(2));
//                rp.setRestaurant(r);
                rp.setRestaurantServerId(cursor.getLong(2));

                rp.setTitle(cursor.getString(3));
                rp.setStartDate(cursor.getLong(4));
                rp.setFinishDate(cursor.getLong(5));
                rp.setDescription(cursor.getString(6));
                rp.setWeight(cursor.getInt(7));

                result.add(rp);
            } while (cursor.moveToNext());
        }

        cursor.close();
        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
        return result;
    }

    public ArrayList<RestaurantPromotion> getValidRestaurantPromotions(String selection, String[] args) {
        if (selection != null && selection.trim().length() > 0) {
            selection += " and ";
        } else {
            selection = "";
        }
        selection += DateUtil.getCurrentTime() + " between " + Schema.RestaurantPromotion.startDate +
                " and " + Schema.RestaurantPromotion.finishDate;

        return getRestaurantPromotions(selection, args);
    }

    public int countValidRestaurantPromotions(String selection, String[] args) {
        if (selection != null && selection.trim().length() > 0) {
            selection += " and ";
        } else {
            selection = "";
        }
        selection += DateUtil.getCurrentTime() + " between " + Schema.RestaurantPromotion.startDate +
                " and " + Schema.RestaurantPromotion.finishDate;

        return countRestaurantPromotions(selection, args);
    }

    public ArrayList<RestaurantPromotion> queryValidPromotions(Restaurant restaurant) {
        if (restaurant != null && restaurant.getServerId() != null) {
            String selection = Schema.RestaurantPromotion.restaurantServerId + " = ? ";
            String[] args = new String[] { restaurant.getServerId().toString() };
            return getValidRestaurantPromotions(selection, args);
        } else {
            return null;
        }
    }

    public boolean hasValidPromotions(Restaurant restaurant) {
        if (restaurant != null && restaurant.getServerId() != null) {
            String selection = Schema.RestaurantPromotion.restaurantServerId + " = ? ";
            String[] args = new String[] { restaurant.getServerId().toString() };
            return countValidRestaurantPromotions(selection, args) > 0;
        } else {
            return false;
        }
    }

    public ArrayList<RestaurantPromotion> queryValidPromotions(String name, RestaurantListFilter filter) {
        ArrayList<Long> restaurantServerIds = queryRestaurantServerIds(name, filter);
        ArrayList<RestaurantPromotion> result = new ArrayList<RestaurantPromotion>();

        if (restaurantServerIds != null && restaurantServerIds.size() > 0) {
            String selection = Schema.RestaurantPromotion.restaurantServerId +
                    " in (" + ArrayUtil.implode(restaurantServerIds, ",") + ")";
            result = getValidRestaurantPromotions(selection, null);
        }

        return result;
    }

    public ArrayList<RestaurantPromotion> queryValidPromotionsAndLoadRestaurants(String name, RestaurantListFilter filter) {
        ArrayList<RestaurantPromotion> result = queryValidPromotions(name, filter);
        loadRestaurantToPromotions(result);

        return result;
    }

    public void saveRestaurantPromotions(List<RestaurantPromotion> restaurantPromotions) {
        if (restaurantPromotions==null || restaurantPromotions.size()==0) { return; }

        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();
        db.beginTransaction();

        try {
            for (RestaurantPromotion rp : restaurantPromotions) {
                ContentValues val=new ContentValues();
                val.put(Schema.RestaurantPromotion.serverId, rp.getServerId());
                val.put(Schema.RestaurantPromotion.restaurantServerId, rp.getRestaurantServerId());
                val.put(Schema.RestaurantPromotion.title, rp.getTitle());
                val.put(Schema.RestaurantPromotion.startDate, rp.getStartDate());
                val.put(Schema.RestaurantPromotion.finishDate, rp.getFinishDate());
                val.put(Schema.RestaurantPromotion.description, rp.getDescription());
                val.put(Schema.RestaurantPromotion.weight, rp.getWeight());

                try {
                    rp.setId(db.insertOrThrow(Schema.RestaurantPromotion._tableName, null, val));
                } catch (SQLiteConstraintException e) {
                    val.remove(Schema.RestaurantPromotion.serverId);
                    db.update(Schema.RestaurantPromotion._tableName, val, Schema.RestaurantPromotion.serverId + "=?",
                            new String[]{rp.getServerId().toString()});
                    rp.setId(getRestaurantIdFromServerId(db, rp.getServerId()));
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
    }

    public void loadRestaurantToPromotions(List<RestaurantPromotion> restaurantPromotionList) {
        if (restaurantPromotionList == null || restaurantPromotionList.size() == 0) return;

        Map<Long, Restaurant> restaurantMap = new HashMap<Long, Restaurant>();

        for (RestaurantPromotion rp : restaurantPromotionList) {
            restaurantMap.put(rp.getRestaurantServerId(), null);
        }
        String inIds = ArrayUtil.implode(restaurantMap.keySet(), ",");

        List<Restaurant> restaurantList = getRestaurantList(Schema.Restaurant.serverId + " in ("
            + inIds + ")", null);
        for (Restaurant r : restaurantList) {
            restaurantMap.put(r.getServerId(), r);
        }

        for (RestaurantPromotion rp : restaurantPromotionList) {
            rp.setRestaurant(restaurantMap.get(rp.getRestaurantServerId()));
        }
    }

    public Boolean verifyIsOpen(RestaurantTimetable restaurantTimetable) {
        if (restaurantTimetable==null) return null;

        Weekday today = DateUtil.getCurrentWeekday();
        String now = new SimpleDateFormat("HH:mm").format(new Date());
        if (restaurantTimetable.getWeekday() == today.getId()) {
            String startTime = restaurantTimetable.getStartTime();
            String finishTime = restaurantTimetable.getFinishTime();
//            if (finishTime.equals("00:00")) finishTime = "24:00";

            if (startTime.compareTo(now) <= 0 && finishTime.compareTo(now) >= 0) {
                return true;
            }
            if (startTime.compareTo(now) >= 0 && now.compareTo(finishTime) <= 0
                    && startTime.compareTo(finishTime) >= 0) {
                return true;
            }
            if (startTime.compareTo(now) <= 0 && startTime.compareTo(finishTime) >= 0) {
                return true;
            }
        }

        return false;
    }

    public void setRestaurantOpenOrClosed(Restaurant r, RestaurantTimetable rtt) {
        int currentWeekday = DateUtil.getCurrentWeekday().getId();
        if (rtt.getWeekday().equals(currentWeekday)) {
            r.setOpen(verifyIsOpen(rtt));
        } else {
            r.setOpen(false);
        }
    }

    public void setRestaurantOpenOrClosed(Restaurant r) {
        if (r.getRestaurantTimetables() == null || r.getRestaurantTimetables().isEmpty()) {
            r.setOpen(null);
            return;
        }
        long currentTime = DateUtil.getCurrentTime() / 60;  // minutos
        int currentWeekday = DateUtil.getCurrentWeekday().getId();
        if (currentTime > r.getLastOpenClosedCalc()) {
            r.setOpen(false);
            for (RestaurantTimetable rtt : r.getRestaurantTimetables()) {
                if (rtt.getWeekday().equals(currentWeekday)) {
                    r.setOpen(verifyIsOpen(rtt));
                    if (r.isOpen()) {
                        break;
                    }
                }
            }
            r.setLastOpenClosedCalc(currentTime);
        }
    }

    public int countAllRestaurants() {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        return DbUtil.count(db, Schema.Restaurant._tableName, null, null);
    }

    public Map<Integer, List<String[]>> getParsedTimetableFor(Restaurant r) {
        if (r.getRestaurantTimetables() == null) {
            return null;
        }

        // Trasladar los horarios tal como son
        HashMap<Integer, List<String[]>> result = new HashMap<Integer, List<String[]>>();
        for (RestaurantTimetable rtt : r.getRestaurantTimetables()) {
            List<String[]> daytt = result.get(rtt.getWeekday());
            if (daytt == null) {
                daytt = new ArrayList<String[]>();
                result.put(rtt.getWeekday(), daytt);
            }
            daytt.add(new String[] {rtt.getStartTime(), rtt.getFinishTime()});
        }

        // Convertir los horarios pegados en "00:00"
        for (int weekday : result.keySet()) {
            List<String[]> daytt = result.get(weekday);
            if (daytt.get(0)[0].equals("00:00")) {
                List<String[]> prevtt = null;
                if (weekday > 1) {
                    prevtt = result.get(weekday - 1);
                } else {
                    prevtt = result.get(7);
                }
                if (prevtt != null) {   // Existe el día anterior
                    if (prevtt.get(prevtt.size() - 1)[1].equals("00:00")) {
                        prevtt.get(prevtt.size() - 1)[1] = daytt.get(0)[1];
                        daytt.remove(0);
                    }
                }
            }
        }

        HashMap<Integer, List<String[]>> finalResult = new HashMap<Integer, List<String[]>>();
        // Dar formato AM/PM
        for (int weekday : result.keySet()) {
            if (result.get(weekday).size() == 0) {
                continue;
            }
            for (String[] tt : result.get(weekday)) {
                tt[0] = DateUtil.from24HToAmPm(tt[0]);
                tt[1] = DateUtil.from24HToAmPm(tt[1]);
            }
            finalResult.put(weekday, result.get(weekday));
        }

        return finalResult;
    }

    public Long getRestaurantServerIdByUri(String uri) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        assert db != null;
        Cursor cursor = db.query(Schema.RestaurantUri._tableName, new String[] {
                Schema.RestaurantUri.restaurantServerId
        }, Schema.RestaurantUri.uri +"=?", new String[] { uri }, null, null, null);

        Long result = null;
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            result = cursor.getLong(0);
        }

        cursor.close();
        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
        return result;
    }

    public void setRestaurantUri(Long restaurantServerId, String uri, String fullUrl) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues val=new ContentValues();
            val.put(Schema.RestaurantUri.restaurantServerId, restaurantServerId);
            val.put(Schema.RestaurantUri.uri, uri);
            val.put(Schema.RestaurantUri.fullUrl, fullUrl);
            int affectedRows = db.update(Schema.RestaurantUri._tableName, val,
                    Schema.RestaurantUri.uri+"=? or "+Schema.RestaurantUri.fullUrl+"=?",
                    new String[] { uri, fullUrl });
            if (affectedRows == 0) {
                db.insert(Schema.RestaurantUri._tableName, null, val);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        } finally {
            db.endTransaction();
        }

        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
    }

    public String getLastRestaurantFullUrlFromServerId(Long serverId) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();
        assert db != null;
        Cursor cursor = db.query(Schema.RestaurantUri._tableName, new String[] {
                Schema.RestaurantUri.fullUrl
        }, Schema.RestaurantUri.restaurantServerId +"=?", new String[] { serverId.toString() },
                null, null, Schema.RestaurantUri.id+" desc", "1");

        String result = null;
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            result = cursor.getString(0);
        }

        cursor.close();
        MikhunaSQLiteOpenHelper.getInstance().safeClose(db);
        return result;
    }

    public boolean restaurantDishLiked(Long dishServerId) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getReadableDatabase();

        Boolean result = DbUtil.queryForBooleanByServerId(db, Schema.RestaurantDish._tableName,
                Schema.RestaurantDish.liked, dishServerId);

        if (result != null) return result;
        else return false;
    }

    public Long getRestaurantDishLikeCountOf(SQLiteDatabase db, Long dishServerId) {
        return DbUtil.queryForLongByServerId(db, Schema.RestaurantDish._tableName,
                Schema.RestaurantDish.likeCount, dishServerId);
    }

    public void likeRestaurantDish(RestaurantDish dish) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();

        ContentValues val = new ContentValues();
        Long currentLikeCount = getRestaurantDishLikeCountOf(db, dish.getServerId());
        if (currentLikeCount == null) currentLikeCount = 0l;
        dish.setLikeCount(currentLikeCount + 1);
        dish.setLiked(true);
        val.put(Schema.RestaurantDish.likeCount, dish.getLikeCount());
        val.put(Schema.RestaurantDish.liked, dish.getLiked());

        db.update(Schema.RestaurantDish._tableName, val, Schema.RestaurantDish.serverId+"=?",
                new String[] {dish.getServerId().toString()});
    }

    public void unlikeRestaurantDish(RestaurantDish dish) {
        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();

        ContentValues val = new ContentValues();
        Long currentLikeCount = getRestaurantDishLikeCountOf(db, dish.getServerId());
        if (currentLikeCount == null || currentLikeCount <= 0) currentLikeCount = 1l;    // Uno para que al restar quede con cero
        dish.setLikeCount(currentLikeCount - 1);
        dish.setLiked(false);
        val.put(Schema.RestaurantDish.likeCount, dish.getLikeCount());
        val.put(Schema.RestaurantDish.liked, dish.getLiked());

        db.update(Schema.RestaurantDish._tableName, val, Schema.RestaurantDish.serverId+"=?",
                new String[] {dish.getServerId().toString()});
    }

//    public void likeRestaurantDish(Long dishServerId) {
//        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();
//
//        ContentValues val = new ContentValues();
//        Long currentLikeCount = getRestaurantDishLikeCountOf(db, dishServerId);
//        if (currentLikeCount == null) currentLikeCount = 0l;
//        val.put(Schema.RestaurantDish.likeCount, currentLikeCount + 1);
//        val.put(Schema.RestaurantDish.liked, true);
//
//        db.update(Schema.RestaurantDish._tableName, val, Schema.RestaurantDish.serverId+"=?",
//                new String[] {dishServerId.toString()});
//    }
//
//    public void unlikeRestaurantDish(Long dishServerId) {
//        SQLiteDatabase db = MikhunaSQLiteOpenHelper.getInstance().getWritableDatabase();
//
//        ContentValues val = new ContentValues();
//        Long currentLikeCount = getRestaurantDishLikeCountOf(db, dishServerId);
//        if (currentLikeCount == null) currentLikeCount = 1l;    // Uno para que al restar quede con cero
//        val.put(Schema.RestaurantDish.likeCount, currentLikeCount - 1);
//        val.put(Schema.RestaurantDish.liked, false);
//
//        db.update(Schema.RestaurantDish._tableName, val, Schema.RestaurantDish.serverId+"=?",
//                new String[] {dishServerId.toString()});
//    }
}
