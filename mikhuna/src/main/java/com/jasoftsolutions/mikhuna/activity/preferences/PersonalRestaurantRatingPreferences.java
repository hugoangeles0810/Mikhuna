package com.jasoftsolutions.mikhuna.activity.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pc07 on 03/06/2014.
 */
public class PersonalRestaurantRatingPreferences {

    public static final String PREFERENCES_NAME = "personal_restaurant_rating";

    public static final String RATING_1 = "rating_1";
    public static final String RATING_2 = "rating_2";
    public static final String RATING_3 = "rating_3";
    public static final String COMMENT = "comment";

    private Context context;
    private SharedPreferences pref;
    private int restaurantServerId;

    public PersonalRestaurantRatingPreferences(Context context, int restaurantServerId) {
        this.context = context;
        this.restaurantServerId = restaurantServerId;
        pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private String parse(String name) {
        return restaurantServerId +  "__" + name;
    }

    public int getRating1() {
        return pref.getInt(parse(RATING_1), 0);
    }

    public int getRating2() {
        return pref.getInt(parse(RATING_2), 0);
    }

    public int getRating3() {
        return pref.getInt(parse(RATING_3), 0);
    }

    public String getComment() {
        return pref.getString(parse(COMMENT), "");
    }

    public void setPref(int rating1, int rating2, int rating3, String comment) {
        pref.edit()
                .putInt(parse(RATING_1), rating1)
                .putInt(parse(RATING_2), rating2)
                .putInt(parse(RATING_3), rating3)
                .putString(parse(COMMENT), comment)
                .commit();
    }
}
