package com.jasoftsolutions.mikhuna.activity.util;

import android.content.Context;

import com.jasoftsolutions.mikhuna.data.AuditManager;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.remote.AuditAction;
import com.jasoftsolutions.mikhuna.remote.AuditSenderTask;

/**
 * Created by pc07 on 08/04/2014.
 */
public class AuditHelper {

    private AuditManager am;
    private Context context;

    public AuditHelper(Context context) {
        this.context = context;
        this.am = new AuditManager();
    }

    public void registerViewProductsOf(Long restaurantServerId){
        registerAuditById(AuditAction.VIEW_PRODUCTS, restaurantServerId);
    }

    public void registerViewDetailOf(Long restaurantServerId) {
        registerAuditById(AuditAction.VIEW_DETAIL_FROM_RESTAURANT_LIST, restaurantServerId);
    }

    public void registerHttpUriViewDetailOf(Long restaurantServerId) {
        registerAuditById(AuditAction.VIEW_DETAIL_FROM_HTTP_URI, restaurantServerId);
    }

    public void registerViewDetailOf(Restaurant restaurant) {
        registerAudit(AuditAction.VIEW_DETAIL_FROM_RESTAURANT_LIST, restaurant);
    }

    public void registerGCMViewDetailOf(Restaurant restaurant) {
        registerAudit(AuditAction.VIEW_DETAIL_FROM_GCM, restaurant);
    }

    public void registerMapIntentOf(Restaurant restaurant) {
        registerAudit(AuditAction.MAP_INTENT, restaurant);
    }

    public void registerDialIntentOf(Restaurant restaurant) {
        registerAudit(AuditAction.DIAL_INTENT, restaurant);
    }

    public void registerRestaurantListAction() {
        registerAudit(AuditAction.RESTAURANT_LIST, null);
    }

    public void registerPromotionListAction() {
        registerAudit(AuditAction.PROMOTION_LIST, null);
    }

    public void registerPromotionListViewDetailActionOf(Restaurant restaurant) {
        registerAudit(AuditAction.VIEW_DETAIL_FROM_PROMOTION_LIST, restaurant);
    }

    public void registerShareRestaurantActionOf(Restaurant restaurant) {
        registerAudit(AuditAction.SHARE_RESTAURANT, restaurant);
    }

    public void registerViewGCM(Restaurant restaurant) {
        registerAudit(AuditAction.VIEW_GCM, restaurant);
    }

    public void registerViewMap(){
        registerAudit(AuditAction.VIEW_MAP, null);
    }

    public void registerPreviewRestaurantFromMap(Restaurant restaurant){
        registerAudit(AuditAction.PREVIEW_RESTAURANT_MAP, restaurant);
    }

    public void registerViewRestaurantFromMap(Long serverId){
        registerAuditById(AuditAction.VIEW_RESTAURANT_FROM_MAP, serverId);
    }

    public void registerShakePhone(){ registerAudit(AuditAction.SHAKE_PHONE, null); }

    public void registerShowRecommended(Long id){ registerAuditById(AuditAction.SHOW_RECOMMENDED, id); }

    public void registerContact() {
        registerAudit(AuditAction.CONTACT, null);
    }

    public void registerAudit(AuditAction auditAction, Restaurant restaurant) {
        Long restaurantServerId = null;
        if (restaurant!=null) {
            restaurantServerId = restaurant.getServerId();
        }
        am.registerAudit(auditAction.getActionId(), restaurantServerId);
        AuditSenderTask senderTask=AuditSenderTask.getInstance(context);
        senderTask.startIfNecessary();
    }

    public void registerAuditById(AuditAction auditAction, Long restaurantServerId) {
        am.registerAudit(auditAction.getActionId(), restaurantServerId);
        AuditSenderTask senderTask=AuditSenderTask.getInstance(context);
        senderTask.startIfNecessary();
    }

}
