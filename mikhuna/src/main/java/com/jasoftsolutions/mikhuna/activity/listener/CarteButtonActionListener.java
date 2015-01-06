package com.jasoftsolutions.mikhuna.activity.listener;

import android.content.Context;
import android.view.View;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.RestaurantCarteActivity;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

/**
 * Created by pc07 on 28/03/14.
 */
public class CarteButtonActionListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        Long restaurantServerId = (Long) view.getTag(R.id.tag_restaurant_server_id);

        try {
            Context context=view.getContext();
            RestaurantCarteActivity.launch(context, restaurantServerId);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }
}