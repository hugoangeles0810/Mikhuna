package com.jasoftsolutions.mikhuna.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pc07 on 03/04/2014.
 */
public class AbstractModel implements Serializable {

    @SerializedName("__id__")
    private Long id;

    @SerializedName("id")
    private Long serverId;

    @SerializedName("lu")
    private Long lastUpdate;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getServerId() {
        return serverId;
    }
    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
