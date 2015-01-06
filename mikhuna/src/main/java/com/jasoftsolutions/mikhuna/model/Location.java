package com.jasoftsolutions.mikhuna.model;

import java.io.Serializable;

/**
 * Created by pc07 on 28/03/14.
 */
public class Location implements Serializable {

    private Double latitude;
    private Double longitude;
    private String label;

    public Location() {}

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(Double latitude, Double longitude, String label) {
        this(latitude, longitude);
        this.label = label;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
