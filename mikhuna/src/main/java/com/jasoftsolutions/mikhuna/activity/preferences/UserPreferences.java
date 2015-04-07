package com.jasoftsolutions.mikhuna.activity.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.jasoftsolutions.mikhuna.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Hugo on 04/03/2015.
 */
public class UserPreferences {

    private static final String PREF_NAME = "user_preferences";
    private static final String USER_EXISTS = "user_exists";
    private static final String USER_NAME_FIRST = "user_name_first";
    private static final String USER_NAME_LAST = "user_name_last";
    private static final String USER_BIRTHDAY = "user_birthday";
    private static final String USER_EMAIL = "user_email";
    private static final String USER_GENDER = "user_gender";
    private static final String USER_IMG_PROFILE_URL = "user_img_profile_url";

    private SharedPreferences mPreferences;

    public UserPreferences(Context context){
        mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(User user){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        mPreferences.edit()
                .putBoolean(USER_EXISTS, true)
                .putString(USER_NAME_FIRST, user.getFirstname())
                .putString(USER_NAME_LAST, user.getLastname())
                .putString(USER_BIRTHDAY, dateFormat.format(user.getBirthday()))
                .putString(USER_EMAIL, user.getEmail())
                .putString(USER_GENDER, user.getGender())
                .putString(USER_IMG_PROFILE_URL, user.getImgUrl())
                .commit();
    }

    public User getUser(){
        if (mPreferences.getBoolean(USER_EXISTS, false)){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            User user = new User();
            user.setFirstname(mPreferences.getString(USER_NAME_FIRST, ""));
            user.setLastname(mPreferences.getString(USER_NAME_LAST, ""));
            user.setEmail(mPreferences.getString(USER_EMAIL, ""));
            try {
                user.setBirthday(dateFormat.parse(mPreferences.getString(USER_BIRTHDAY, null)));
            } catch (ParseException e) {
                user.setBirthday(null);
            }
            user.setGender(mPreferences.getString(USER_GENDER, ""));
            user.setImgUrl(mPreferences.getString(USER_IMG_PROFILE_URL, ""));

            return user;
        }

        return null;
    }
}
