package com.jasoftsolutions.mikhuna.activity.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.RestaurantCarteActivity;
import com.jasoftsolutions.mikhuna.activity.RestaurantDishCategoryActivity;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

/**
 * Created by pc07 on 28/03/14.
 */
public class CarteButtonActionListener implements View.OnClickListener {

    private Restaurant restaurant;

    public CarteButtonActionListener(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void onClick(View view) {
        Long restaurantServerId = (Long) view.getTag(R.id.tag_restaurant_server_id);

        try {
            Context context=view.getContext();
            Intent i = RestaurantDishCategoryActivity.getLauncherIntent(context, restaurant);
            context.startActivity(i);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }
}