package com.jasoftsolutions.mikhuna.activity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pc07 on 16/04/2014.
 */
public class AppPreferences {


    public static SharedPreferences get(Context context) {
        return context.getSharedPreferences("app", Context.MODE_PRIVATE);
    }

}
