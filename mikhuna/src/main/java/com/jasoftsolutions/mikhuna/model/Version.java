package com.jasoftsolutions.mikhuna.model;

/**
 * Created by Hugo on 10/04/2015.
 */
public class Version {

    public static final Integer VERSION_OK = 1;

    private Integer state;
    private String url;
    private String message;

    public Version() {
    }

    public Version(Integer state, String url, String message) {
        this.state = state;
        this.url = url;
        this.message = message;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }
}
