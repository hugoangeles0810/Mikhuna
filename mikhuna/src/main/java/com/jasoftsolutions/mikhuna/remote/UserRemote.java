package com.jasoftsolutions.mikhuna.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jasoftsolutions.mikhuna.model.User;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo on 17/03/2015.
 */
public class UserRemote {

    public static final String MALE = "male";
    public static final String FEMALE = "female";

    public Boolean saveUser(User user){

        JsonObject userJson = parseUserToJsonObject(user);
        JsonElement response = null;
        RemoteHandler rh = new RemoteHandler();
        Gson gson = new Gson();

        List<NameValuePair> params = new ArrayList();
        NameValuePair userValuePair = new BasicNameValuePair("data", userJson.toString());
        params.add(userValuePair);
        try{
            response = rh.postResourceFromUrl("/saveEndUser/", params, JsonElement.class);
            if (response != null){
                return gson.fromJson(response.getAsJsonObject().get("success"), Boolean.class);
            }
        } catch (Exception e){
            ExceptionUtil.ignoreException(e);
        }

        return null;
    }

    private JsonObject parseUserToJsonObject(User user){
        JsonObject userJson = new JsonObject();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        userJson.addProperty("email", user.getEmail());
        userJson.addProperty("names", user.getFirstname());
        userJson.addProperty("last_names", user.getLastname());
        userJson.addProperty("sex", user.getGender().equals(MALE)?1:0);
        userJson.addProperty("birthday", df.format(user.getBirthday()));
        userJson.addProperty("url_image", user.getImgUrl());
        userJson.addProperty("type_login", user.getLoginType());
        userJson.addProperty("uid_fb", user.getUid());
        userJson.addProperty("password", user.getPassword());
        return userJson;
    }

}
