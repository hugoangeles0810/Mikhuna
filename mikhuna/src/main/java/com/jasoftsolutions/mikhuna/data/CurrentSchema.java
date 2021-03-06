package com.jasoftsolutions.mikhuna.data;

/**
 * Created by pc07 on 25/04/2014.
 */
public class CurrentSchema {

    public static final String[] DEFINITION = new String[] {
//            "create table "+Schema.ServiceType._tableName+" (" +
//                    Schema.ServiceType.id + " integer primary key," +
//                    Schema.ServiceType.serverId + " integer unique," +
//                    Schema.ServiceType.name + " varchar(255)" +
//            ")",
            "create table "+Schema.Ubigeo._tableName+" (" +
                    Schema.Ubigeo.id + " integer primary key," +
                    Schema.Ubigeo.serverId + " integer unique," +
                    Schema.Ubigeo.parentUbigeoServerId + " integer," +
                    Schema.Ubigeo.ubigeoCategoryId + " integer," +
                    Schema.Ubigeo.name + " varchar(255)," +
                    Schema.Ubigeo.latitude + " double," +
                    Schema.Ubigeo.longitude + " double," +
                    "foreign key ("+Schema.Ubigeo.parentUbigeoServerId +") " +
                        "references " + Schema.Ubigeo._tableName + "(" + Schema.Ubigeo.serverId + ")" +
            ")",
            "create table "+Schema.Restaurant._tableName+" (" +
                    Schema.Restaurant.id + " integer primary key," +
                    Schema.Restaurant.serverId + " integer unique," +
                    Schema.Restaurant.name + " varchar(255) not null," +
                    Schema.Restaurant.description + " text," +
                    Schema.Restaurant.phoneNumber + " varchar(50)," +
                    Schema.Restaurant.currency + " varchar(10)," +
                    Schema.Restaurant.minAmount + " float," +
                    Schema.Restaurant.rating + " float," +
                    Schema.Restaurant.imageId + " integer," +
                    Schema.Restaurant.minDeliveryTime + " integer," +
                    Schema.Restaurant.maxDeliveryTime + " integer," +
                    Schema.Restaurant.latitude + " double," +
                    Schema.Restaurant.longitude + " double," +
                    Schema.Restaurant.address + " varchar(255)," +
                    Schema.Restaurant.shippingCost + " float," +
                    Schema.Restaurant.lastUpdate + " datetime," +
                    Schema.Restaurant.logoUrl + " varchar(1024)," +
                    Schema.Restaurant.smallLogoUrl + " varchar(1024)," +
                    Schema.Restaurant.weight + " integer," +
                    Schema.Restaurant.serviceTypeId + " integer," +
                    Schema.Restaurant.ubigeoServerId + " integer," +
                    Schema.Restaurant.timetableDescription + " text," +
                    "foreign key ("+Schema.Restaurant.ubigeoServerId +") " +
                        "references " + Schema.Ubigeo._tableName + "(" + Schema.Ubigeo.serverId + ")" +
            ")",
            "create table "+Schema.RestaurantTimeTable._tableName+" (" +
                    Schema.RestaurantTimeTable.id + " integer primary key," +
                    Schema.RestaurantTimeTable.serverId + " integer unique," +
                    Schema.RestaurantTimeTable.restaurantId + " integer," +
                    Schema.RestaurantTimeTable.weekday + " varchar(255)," +
                    Schema.RestaurantTimeTable.startTime + " varchar(10)," +
                    Schema.RestaurantTimeTable.finishTime + " varchar(10)," +
                    Schema.RestaurantTimeTable.lastUpdate + " datetime," +
                    "foreign key ("+Schema.RestaurantTimeTable.restaurantId+") " +
                        "references " + Schema.Restaurant._tableName + "(" + Schema.Restaurant.id + ")" +
            ")",
            "create table "+Schema.Audit._tableName+" (" +
                    Schema.Audit.id + " integer primary key," +
                    Schema.Audit.actionId + " integer," +
                    Schema.Audit.actionDate + " datetime," +
                    Schema.Audit.restaurantServerId + " integer," +
                    Schema.Audit.sent + " boolean," +
                    "foreign key ("+Schema.Audit.restaurantServerId+") " +
                        "references " + Schema.Restaurant._tableName + "(" + Schema.Restaurant.serverId + ")" +
            ")",
            "create table "+ Schema.RestaurantCategory._tableName+" (" +
                    Schema.RestaurantCategory.id + " integer primary key," +
                    Schema.RestaurantCategory.serverId + " integer unique," +
                    Schema.RestaurantCategory.parentUbigeoServerId + " integer," +
                    Schema.RestaurantCategory.name + " varchar(255)" +
            ")",
            "create table "+ Schema.RestaurantCategoryAssign._tableName+" (" +
                    Schema.RestaurantCategoryAssign.id + " integer primary key," +
                    Schema.RestaurantCategoryAssign.serverId + " integer unique," +
                    Schema.RestaurantCategoryAssign.restaurantId + " integer," +
                    Schema.RestaurantCategoryAssign.restocatServerId + " integer," +
                    "foreign key ("+ Schema.RestaurantCategoryAssign.restaurantId+") " +
                        "references " + Schema.Restaurant._tableName + "(" + Schema.Restaurant.id + ")," +
                    "foreign key ("+ Schema.RestaurantCategoryAssign.restocatServerId +") " +
                        "references " + Schema.RestaurantCategory._tableName + "(" + Schema.RestaurantCategory.id + ")" +
            ")",
//            "create table "+Schema.RestaurantServiceType._tableName+" (" +
//                    Schema.RestaurantServiceType.id + " integer primary key," +
//                    Schema.RestaurantServiceType.serverId + " integer unique," +
//                    Schema.RestaurantServiceType.restaurantId + " integer," +
//                    Schema.RestaurantServiceType.serviceTypeId + " integer," +
//                    "foreign key ("+Schema.RestaurantServiceType.restaurantId+") " +
//                        "references " + Schema.Restaurant._tableName + "(" + Schema.Restaurant.id + ")," +
//                    "foreign key ("+Schema.RestaurantServiceType.serviceTypeId+") " +
//                        "references " + Schema.ServiceType._tableName + "(" + Schema.ServiceType.id + ")" +
//            ")",
            // Version 3
            "create table "+Schema.RestaurantPromotion._tableName+" (" +
                    Schema.RestaurantPromotion.id + " integer primary key," +
                    Schema.RestaurantPromotion.serverId + " integer unique," +
                    Schema.RestaurantPromotion.restaurantServerId + " integer," +
                    Schema.RestaurantPromotion.title + " text," +
                    Schema.RestaurantPromotion.startDate + " integer," +
                    Schema.RestaurantPromotion.finishDate + " integer," +
                    Schema.RestaurantPromotion.description + " text," +
                    Schema.RestaurantPromotion.weight + " integer," +
                    "foreign key ("+Schema.RestaurantPromotion.restaurantServerId +") " +
                        "references " + Schema.Restaurant._tableName + "(" + Schema.Restaurant.serverId + ")" +
            ")",
            // Version 4
            "create table "+Schema.SendServerQueue._tableName+" (" +
                    Schema.SendServerQueue.id + " integer primary key," +
                    Schema.SendServerQueue.url + " text," +
                    Schema.SendServerQueue.method + " text," +
                    Schema.SendServerQueue.data + " text," +
                    Schema.SendServerQueue.tag + " text," +
                    Schema.SendServerQueue.priority + " integer," +
                    Schema.SendServerQueue.wifiOnly + " integer" +
            ")",
            // Version 5
            "create table "+ Schema.RestaurantUri._tableName+" (" +
                    Schema.RestaurantUri.id + " integer primary key," +
                    Schema.RestaurantUri.uri + " text," +
                    Schema.RestaurantUri.fullUrl + " text," +
                    Schema.RestaurantUri.restaurantServerId + " integer" +
            ")",
            // Version 6
            "create table "+ Schema.RestaurantDishCategory._tableName+" (" +
                    Schema.RestaurantDishCategory.id + " integer primary key," +
                    Schema.RestaurantDishCategory.serverId + " integer unique," +
                    Schema.RestaurantDishCategory.restaurantServerId + " integer," +
                    Schema.RestaurantDishCategory.name + " text," +
                    Schema.RestaurantDishCategory.position + " integer" +
                    Schema.RestaurantDishCategory.dishesLastUpdate + " integer" +
            ")",
            "create table "+ Schema.RestaurantDish._tableName+" (" +
                    Schema.RestaurantDish.id + " integer primary key," +
                    Schema.RestaurantDish.serverId + " integer unique," +
                    Schema.RestaurantDish.restaurantServerId + " integer," +
                    Schema.RestaurantDish.restaurantDishCategoryServerId + " integer," +
                    Schema.RestaurantDish.name + " text," +
                    Schema.RestaurantDish.description + " text," +
                    Schema.RestaurantDish.position + " integer," +
                    Schema.RestaurantDish.price + " float" +
                    Schema.RestaurantDish.liked + " integer," +
                    Schema.RestaurantDish.likeCount + " integer" +
            ")",
            "create table " + Schema.RestaurantDishPresentation._tableName+" (" +
                    Schema.RestaurantDishPresentation.id + " integer primary key," +
                    Schema.RestaurantDishPresentation.restaurantDishServerId + " integer," +
                    Schema.RestaurantDishPresentation.name + " text, " +
                    Schema.RestaurantDishPresentation.position + " integer," +
                    Schema.RestaurantDishPresentation.cost + " float" +
                    ")",
            "alter table "+ Schema.Restaurant._tableName + " add " +
                    Schema.Restaurant.numberProductCategory +  " integer ",
            "alter table "+ Schema.Restaurant._tableName + " add " +
                    Schema.Restaurant.categoryLastUpdate +  " integer default 0",
            // Version 7
            "alter table"+ Schema.RestaurantDishCategory._tableName + " add " +
                    Schema.RestaurantDishCategory.description + " text",
            // Version 8
            "alter table "+ Schema.Ubigeo._tableName + " add " +
                    Schema.Ubigeo.latitude + " double ",
            "alter table "+ Schema.Ubigeo._tableName + " add " +
                    Schema.Ubigeo.longitude + " double ",
            // Version 9
            "create table "+ Schema.TempLikeDish._tableName+" (" +
                    Schema.TempLikeDish.id + " integer primary key,"+
                    Schema.TempLikeDish.dishId + " integer unique,"+
                    Schema.TempLikeDish.likeState + " integer"+
                    ")",
            // Version 10
            "alter table "+ Schema.Restaurant._tableName + " add " +
                    Schema.Restaurant.state + " integer default 0",
            "alter table "+ Schema.RestaurantDishCategory._tableName + " add " +
                    Schema.RestaurantDishCategory.state + " integer default 0",
            "alter table "+ Schema.RestaurantDish._tableName + " add " +
                    Schema.RestaurantDish.state + " integer default 0"
    };

}
