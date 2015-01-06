package com.jasoftsolutions.mikhuna.remote.json;

/**
 * Created by pc07 on 08/04/2014.
 */
public class JsonResponse {

    private Boolean success;

    private String message;

    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
