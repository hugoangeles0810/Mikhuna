package com.jasoftsolutions.mikhuna.activity.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.ArgKeys;
import com.jasoftsolutions.mikhuna.activity.RestaurantCarteActivity;
import com.jasoftsolutions.mikhuna.activity.RestaurantDishCategoryActivity;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;
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

            new AuditHelper(view.getContext()).registerViewProductsOf(restaurantServerId);
            AnalyticsUtil.registerEvent(view.getContext(), AnalyticsConst.Category.DETAIL_RESTAURANT,
                    AnalyticsConst.Action.VIEW_PRODUCTS, restaurant.getServerId().toString());
            Intent i = RestaurantDishCategoryActivity.getLauncherIntent(context, restaurant);
            context.startActivity(i);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }
}