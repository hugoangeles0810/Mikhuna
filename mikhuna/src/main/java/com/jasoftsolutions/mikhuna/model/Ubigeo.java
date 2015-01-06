package com.jasoftsolutions.mikhuna.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pc07 on 25/04/2014.
 */
public class Ubigeo extends AbstractModel {

    @SerializedName("ui")
    private Long parentUbigeoServerId;

    private Ubigeo parentUbigeo;

    @SerializedName("uci")
    private Integer ubigeoCategoryId;

    @SerializedName("n")
    private String name;

    public Long getParentUbigeoServerId() {
        return parentUbigeoServerId;
    }
    public void setParentUbigeoServerId(Long parentUbigeoServerId) {
        this.parentUbigeoServerId = parentUbigeoServerId;
    }

    public Ubigeo getParentUbigeo() {
        return parentUbigeo;
    }
    public void setParentUbigeo(Ubigeo parentUbigeo) {
        this.parentUbigeo = parentUbigeo;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getUbigeoCategoryId() {
        return ubigeoCategoryId;
    }
    public void setUbigeoCategoryId(Integer ubigeoCategoryId) {
        this.ubigeoCategoryId = ubigeoCategoryId;
    }
}
