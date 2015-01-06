package com.jasoftsolutions.mikhuna.activity.listener;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.util.RestaurantViewUtil;
import com.jasoftsolutions.mikhuna.model.RestaurantDish;
import com.jasoftsolutions.mikhuna.store.RestaurantStore;
import com.jasoftsolutions.mikhuna.store.StoreAdapter;

/**
 * Created by pc07 on 27/03/14.
 */
public class LikeButtonListener implements View.OnClickListener {

    @Override
    public void onClick(final View view) {
        Context context=view.getContext();
        final RestaurantDish dish = (RestaurantDish) view.getTag(R.id.tag_restaurant_dish);

        final TextView likeCount = (TextView) view.getTag(R.id.tag_restaurant_dish_like_count_text);
//        new AuditHelper(view.getContext()).registerDialIntentOf(restaurant);

        RestaurantStore.getInstance().toggleLikeRestaurantDish(dish, new StoreAdapter() {
            @Override
            public void onSuccess(Object sender, Object data) {
                super.onSuccess(sender, data);
                    if (view instanceof ImageView) {
                        ((Activity)view.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RestaurantViewUtil.showRestaurantDishLike(dish, (ImageView)view, likeCount);
                            }
                        });
                    }
            }
        });
    }
}