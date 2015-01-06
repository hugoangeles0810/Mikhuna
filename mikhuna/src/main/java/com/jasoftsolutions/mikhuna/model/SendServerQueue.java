package com.jasoftsolutions.mikhuna.model;

/**
 * Created by pc07 on 03/06/2014.
 */
public class SendServerQueue {

    private long id;
    private String url;
    private String method;
    private String data;
    private String tag;
    private int priority;
    private boolean wifiOnly;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isWifiOnly() {
        return wifiOnly;
    }
    public void setWifiOnly(boolean wifiOnly) {
        this.wifiOnly = wifiOnly;
    }
}
