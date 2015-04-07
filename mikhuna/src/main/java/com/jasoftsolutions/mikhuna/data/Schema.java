package com.jasoftsolutions.mikhuna.data;

import android.provider.BaseColumns;

/**
 * Created by pc07 on 01/04/2014.
 */
public final class Schema {

    public static class BaseTable {
        public static final String
            id = BaseColumns._ID,
            serverId = "sever_id",
            lastUpdate = "last_update"
        ;
    }

    public static final class Restaurant extends BaseTable {
        public static final String
            _tableName = "restaurant",
            //
            name = "name",
            description = "description",
            phoneNumber = "phone_number",
            minAmount = "min_amount",
            rating = "rating",
            imageId = "image_id",
            minDeliveryTime = "min_delivery_time",
            maxDeliveryTime = "max_delivery_time",
            latitude = "latitude",
            longitude = "longitude",
            address = "address",
            shippingCost = "shipping_cost",
            currency = "currency",
            logoUrl = "logo_url",
            smallLogoUrl = "small_logo_url",
            weight = "weight",
            serviceTypeId = "service_type_id",
            timetableDescription = "timetable_description",
            numberProductCategory = "number_product_category",
            ubigeoServerId = "ubigeo_server_id",
            categoryLastUpdate = "category_last_update",
            likeDishLastUpdate = "like_dish_last_update",
            state = "state",
            imageUrl = "image_url",
            orientation = "orientation"
        ;
    }

    // Carta de restaurantes

    public static final class RestaurantDishCategory extends BaseTable {
        public static final String
            _tableName = "restaurant_dish_category",
            //
            restaurantServerId = "restaurant_server_id",
            name = "name",
            description = "description",
            position = "position",
            dishesLastUpdate = "dishes_last_update",
            state = "state"
        ;
    }

    public static final class RestaurantDish extends BaseTable {
        public static final String
            _tableName = "restaurant_dish",
            //
            restaurantServerId = "restaurant_server_id",
            restaurantDishCategoryServerId = "restaurant_dish_category_server_id",
            name = "name",
            description = "description",
            position = "position",
            price = "price",
            liked = "liked",
            likeCount = "like_count",
            state = "state"
        ;
    }

    public static final class RestaurantDishPresentation extends BaseTable {
        public static final String
            _tableName = "restaurant_dish_presentation",
           restaurantDishServerId = "restaurant_dish_server_id",
           name = "name",
           position = "position",
           cost = "cost",
           state = "state"
        ;
    }

    public static final class Service extends BaseTable{
        public  static final String
            _tableName = "service",
            name = "name",
            position = "position",
            state = "state";

    }

    public static final class Pay extends BaseTable{
        public static final String
            _tableName = "pay",
            nameFile = "name_file",
            position = "position",
            state = "state";

    }

    public static final class Links extends BaseTable{
        public static final String
            _tableName = "links",
            link = "link",
            typeLink = "type_link",
            state = "state",
            restaurantId = "restaurant_id";

    }

    public static final class RestaurantService extends BaseTable{
        public  static final String
                _tableName = "restaurant_service",
                restaurantId = "restaurant_id",
                serviceId = "service_id";
    }

    public static final class RestaurantPay extends BaseTable{
        public static final String
                _tableName = "restaurant_pay",
                restaurantId = "restaurant_id",
                payId = "pay_id";
    }



    public static final class RestaurantTimeTable extends BaseTable {
        public static final String
            _tableName = "restaurant_timetable",
            //
            restaurantId = "restaurant_id",
            weekday = "weekday",
            startTime = "start_time",
            finishTime = "finish_time"
        ;
    }

    public static final class RestaurantCategoryAssign extends BaseTable {
        public static final String
            _tableName = "restaurant_restocat",
            //
            restaurantId = "restaurant_id",
            restocatServerId = "restocat_server_id"
        ;
    }

    public static final class RestaurantServiceType extends BaseTable {
        public static final String
            _tableName = "restaurant_service_type",
            //
            restaurantId = "restaurant_id",
            serviceTypeId = "service_type_id"
        ;
    }

    public static final class RestaurantPromotion extends BaseTable {   // v6
        public static final String
            _tableName = "restaurant_promotion",
            //
            restaurantServerId = "restaurant_server_id",
            title = "title",
            startDate = "start_date",
            finishDate = "finish_date",
            description = "description",
            weight = "weight"
        ;
    }

    // Tablas de mantenimiento

    public static final class Ubigeo extends BaseTable {
        public final static String
            _tableName = "ubigeo",
            //
            parentUbigeoServerId = "parent_ubigeo_server_id",
            ubigeoCategoryId = "ucat_id",
            name = "name",
            latitude = "latitude",
            longitude = "longitude"
        ;
    }

    public static final class RestaurantCategory extends BaseTable {
        public final static String
            _tableName = "restocat",
            //
            parentUbigeoServerId = "parent_ubigeo_server_id",
            name = "name"
        ;
    }

    public static final class ServiceType extends BaseTable {
        public final static String
            _tableName = "service_type",
            //
            name = "name"
        ;
    }

    // Auditoría

    public static final class Audit {
        public static final String
            _tableName = "audit",
        //
            id = BaseColumns._ID,
            actionId = "action_id",
            actionDate = "action_date",
            restaurantServerId = "restaurant_server_id",
            params = "params",  // parámetros extra sobre esta acción
            sent = "sent"   // Indica si ya ha sido enviado al servidor
        ;
    }

    // Cola de envío de datos al servidor

    public static final class SendServerQueue {
        public static final String
            _tableName = "send_server_queue",
        //
            id = BaseColumns._ID,
            url = "uri",
            method = "method",
            data = "data",
            tag = "tag",
            priority = "priority",
            wifiOnly = "wifi_only"   // Indica si sólo se debe enviar con una conexión wifi
        ;
    }

    // Mapeo de urls de los restaurantes
    public static final class RestaurantUri {
        public static final String
            _tableName = "restaurant_uri",

            id = BaseColumns._ID,
            uri = "uri",
            fullUrl = "full_url",
            restaurantServerId = "restaurant_server_id"
        ;
    }

    // Tabla de data temporal de likes en los platos
    public static final class TempLikeDish{
        public static final String
            _tableName = "temp_like_dish",

            id = BaseColumns._ID,
            dishId = "dish_id",
            likeState = "like_state";
    }
}
