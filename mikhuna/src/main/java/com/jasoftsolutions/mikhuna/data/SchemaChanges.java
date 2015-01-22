package com.jasoftsolutions.mikhuna.data;

/**
 * Created by pc07 on 25/04/2014.
 */
public class SchemaChanges {

    public static final String[][] UPGRADES = new String[][] {
            new String[] {   // Versión 1
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
                            Schema.Restaurant.serviceTypeId + " integer" +
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
                            ")"
            },
            new String[] {  // Version 2
                    "create table "+Schema.Ubigeo._tableName+" (" +
                            Schema.Ubigeo.id + " integer primary key," +
                            Schema.Ubigeo.serverId + " integer unique," +
                            Schema.Ubigeo.parentUbigeoServerId + " integer," +
                            Schema.Ubigeo.ubigeoCategoryId + " integer," +
                            Schema.Ubigeo.name + " varchar(255)," +
                            "foreign key ("+Schema.Ubigeo.parentUbigeoServerId +") " +
                                "references " + Schema.Ubigeo._tableName + "(" + Schema.Ubigeo.serverId + ")" +
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
                    "alter table " + Schema.Restaurant._tableName +
                            " add " + Schema.Restaurant.ubigeoServerId + " integer",
            },
            new String[] {  // Version 3
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
            },
            new String[] {  // Version 4
                    "create table "+Schema.SendServerQueue._tableName+" (" +
                            Schema.SendServerQueue.id + " integer primary key," +
                            Schema.SendServerQueue.url + " text," +
                            Schema.SendServerQueue.method + " text," +
                            Schema.SendServerQueue.data + " text," +
                            Schema.SendServerQueue.tag + " text," +
                            Schema.SendServerQueue.priority + " integer," +
                            Schema.SendServerQueue.wifiOnly + " integer" +
                            ")",
            },
            new String[] {  // Version 5
                    "create table "+ Schema.RestaurantUri._tableName+" (" +
                            Schema.RestaurantUri.id + " integer primary key," +
                            Schema.RestaurantUri.uri + " text," +
                            Schema.RestaurantUri.fullUrl + " text," +
                            Schema.RestaurantUri.restaurantServerId + " integer" +
                            ")",
                    "alter table " + Schema.Restaurant._tableName +
                            " add " + Schema.Restaurant.timetableDescription + " text",
            },
            new String[] {  // Version 6
                    "create table "+ Schema.RestaurantDishCategory._tableName+" (" +
                            Schema.RestaurantDishCategory.id + " integer primary key," +
                            Schema.RestaurantDishCategory.serverId + " integer unique," +
                            Schema.RestaurantDishCategory.restaurantServerId + " integer," +
                            Schema.RestaurantDishCategory.name + " text," +
                            Schema.RestaurantDishCategory.position + " integer," +
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
                            Schema.RestaurantDish.price + " float," +
                            Schema.RestaurantDish.liked + " integer," +
                            Schema.RestaurantDish.likeCount + " integer" +
                            ")",
                    "create table " + Schema.RestaurantDishPresentation._tableName+" (" +
                            Schema.RestaurantDishPresentation.id + " integer primary key," +
                            Schema.RestaurantDishPresentation.serverId + " integer unique," +
                            Schema.RestaurantDishPresentation.restaurantDishServerId + " integer," +
                            Schema.RestaurantDishPresentation.name + " text, " +
                            Schema.RestaurantDishPresentation.position + " integer," +
                            Schema.RestaurantDishPresentation.cost + " float" +
                            ")",
                    "alter table "+ Schema.Restaurant._tableName + " add " +
                            Schema.Restaurant.numberProductCategory +  " integer ",
                    "alter table "+ Schema.Restaurant._tableName + " add " +
                            Schema.Restaurant.categoryLastUpdate +  " integer default 0",

            },
            new String[]{ //Version 7
                    "alter table "+ Schema.RestaurantDishCategory._tableName + " add " +
                            Schema.RestaurantDishCategory.description + " text ",
                    "create table "+ Schema.Service._tableName+" (" +
                            Schema.Service.id + " integer primary key," +
                            Schema.Service.serverId + " integer unique,"+
                            Schema.Service.name + " text,"+
                            Schema.Service.position + " integer," +
                            Schema.Service.state + " integer" +
                            ")",
                    "create table "+ Schema.Pay._tableName+" (" +
                            Schema.Pay.id + " integer primary key," +
                            Schema.Pay.serverId + " integer unique," +
                            Schema.Pay.nameFile + " text," +
                            Schema.Pay.position + " integer," +
                            Schema.Pay.state + " integer" +
                            ")",
                    "create table "+ Schema.RestaurantService._tableName+" (" +
                            Schema.RestaurantService.id + " integer primary key," +
                            Schema.RestaurantService.serverId + " integer unique," +
                            Schema.RestaurantService.restaurantId + " integer," +
                            Schema.RestaurantService.serviceId + " integer" +
                            ")",
                    "create table "+ Schema.RestaurantPay._tableName+" (" +
                            Schema.RestaurantPay.id + " integer primary key,"+
                            Schema.RestaurantPay.serverId + " integer unique,"+
                            Schema.RestaurantPay.restaurantId + " integer,"+
                            Schema.RestaurantPay.payId + " integer" +
                    ")",
                    "create table "+ Schema.Links._tableName+" (" +
                            Schema.Links.id + " integer primary key,"+
                            Schema.Links.serverId + " integer unique,"+
                            Schema.Links.link + " text,"+
                            Schema.Links.typeLink + " integer," +
                            Schema.Links.restaurantId + " integer" +
                            ")",
}
    };

    public static final String[][] DOWNGRADES = new String[][] {
            new String[] {  // De 2 a 1
                    "drop table if exists "+ Schema.RestaurantCategoryAssign._tableName,
                    "drop table if exists "+ Schema.RestaurantCategory._tableName,
                    "drop table if exists "+Schema.Ubigeo._tableName,
            },
            new String[] {  // De 3 a 2
                    "drop table if exists "+ Schema.RestaurantPromotion._tableName,
            },
            new String[] {  // De 4 a 3
                    "drop table if exists "+ Schema.SendServerQueue._tableName,
            },
            new String[] {  // De 5 a 4
                    "drop table if exists "+ Schema.RestaurantUri._tableName,
            },
            new String[] {  // De 6 a 5
                    "drop table if exists "+ Schema.RestaurantDishCategory._tableName,
                    "drop table if exists "+ Schema.RestaurantDish._tableName,
                    "drop table if exists "+ Schema.RestaurantDishPresentation._tableName,
                    "alter table "+ Schema.Restaurant._tableName + " drop " +
                            Schema.Restaurant.numberProductCategory,
                    "alter table "+ Schema.Restaurant._tableName + " drop " +
                            Schema.Restaurant.categoryLastUpdate
            },
            new String[]{ // De 7 a 6
                    "alter table "+ Schema.RestaurantDishCategory._tableName + " drop " +
                            Schema.RestaurantDishCategory.description,
                    "drop table if exists "+ Schema.RestaurantService._tableName,
                    "drop table if exists "+ Schema.RestaurantPay._tableName,
                    "drop table if exists "+ Schema.Service._tableName,
                    "drop table if exists "+ Schema.Pay._tableName,
                    "drop table if exists "+ Schema.Links._tableName
            }
    };

}
