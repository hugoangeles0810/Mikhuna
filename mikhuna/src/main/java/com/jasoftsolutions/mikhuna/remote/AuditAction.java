package com.jasoftsolutions.mikhuna.remote;

/**
 * Created by pc07 on 08/04/2014.
 */
public enum AuditAction {

    VIEW_DETAIL_FROM_RESTAURANT_LIST(1),
    DIAL_INTENT(2),
    MAP_INTENT(3),
    RESTAURANT_LIST(4),
    PROMOTION_LIST(5),
    VIEW_DETAIL_FROM_PROMOTION_LIST(6),
    SHARE_RESTAURANT(7),
    VIEW_GCM(8),
    VIEW_DETAIL_FROM_GCM(9),
    INSTALL(10),
    CONTACT(11),
    VIEW_DETAIL_FROM_HTTP_URI(12),
    INSTALL_GCM(15),
    VIEW_PRODUCTS(16),
    VIEW_MAP(17),
    PREVIEW_RESTAURANT_MAP(18),
    VIEW_RESTAURANT_FROM_MAP(19),
    SHAKE_PHONE(20),
    SHOW_RECOMMENDED(21),
    ;

    private int actionId;

    AuditAction(int actionId) {
        this.actionId = actionId;
    }

    public int getActionId() {
        return actionId;
    }
}
