package com.jasoftsolutions.mikhuna.domain;

import com.jasoftsolutions.mikhuna.R;

/**
 * Created by pc07 on 25/04/2014.
 */
public enum UbigeoCategory {

    COUNTRY(1, R.string.ubigeo_category_country),
    LOCALITY(2, R.string.ubigeo_category_locality);

    private int id;
    private int resourceId;

    UbigeoCategory(int id, int resourceId) {
        this.id = id;
        this.resourceId = resourceId;
    }

    public static UbigeoCategory getFromId(int id) {
        if (1 == id) return COUNTRY;
        else if (2 == id) return LOCALITY;
        return null;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getResourceId() {
        return resourceId;
    }
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
