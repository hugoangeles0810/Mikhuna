package com.jasoftsolutions.mikhuna.activity.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.domain.RestaurantServiceType;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.RestaurantDish;
import com.jasoftsolutions.mikhuna.model.RestaurantPromotion;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.util.ArrayUtil;
import com.jasoftsolutions.mikhuna.util.DateUtil;
import com.jasoftsolutions.mikhuna.util.ResourcesUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pc07 on 27/03/14.
 */
public class RestaurantViewUtil {

    private static final String TAG =  RestaurantViewUtil.class.getSimpleName();

    public static Drawable getRestaurantLogoFromImageId(Context context, Integer imageId) {
        try {
            return ResourcesUtil.getDrawableByName(context, "restaurant_default_icon_" + imageId);
        } catch (Resources.NotFoundException nfe) {
            return ResourcesUtil.getDrawableByName(context, "restaurant_default_icon_default");
        }
    }

    public static Drawable getRestaurantImageFromImageId(Context context, Integer imageId){
        try{
            return ResourcesUtil.getDrawableByName(context, "type_" + imageId);
        }catch (Resources.NotFoundException nfe){
            return ResourcesUtil.getDrawableByName(context, "type_1");
        }
    }

    public static void displaySmallLogo(Restaurant restaurant, ImageView imageView) {
        if (restaurant.getImageId()!=null) {
            imageView.setImageDrawable(getRestaurantLogoFromImageId(imageView.getContext(), restaurant.getImageId()));
        }
        if (restaurant.getSmallLogoUrl()!=null) {
            String absoluteUrl = Const.BACKEND_BASE_URL + restaurant.getSmallLogoUrl();
            ImageLoader.getInstance().displayImage(absoluteUrl, imageView);
        }
    }

    public static void displayRestaurantImage(Restaurant restaurant, ImageView imageView){
        if (restaurant.getImageId() != null){
            imageView.setImageDrawable(getRestaurantImageFromImageId(imageView.getContext(), restaurant.getImageId()));
        }

        if (restaurant.getImageUrl() != null){
            Log.i("Image", "URL: " + restaurant.getImageUrl());
            String absoluteUrl = Const.BACKEND_BASE_URL + restaurant.getImageUrl();
            Log.i("Image", "URL: " + absoluteUrl);
            ImageLoader.getInstance().displayImage(absoluteUrl, imageView);
        }
    }

    public static void setRating(Restaurant restaurant, RatingBar ratingBar) {
        ratingBar.setMax(50);
        if (restaurant.getRating()!=null) {
            ratingBar.setProgress((int)(restaurant.getRating()*10));
        }
    }

    public static void showRestaurantIsOpen(Restaurant restaurant, TextView textView) {
        if (restaurant.isOpen()==null) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
            if (restaurant.isOpen()) {
                textView.setTextAppearance(textView.getContext(), R.style.restaurant_open);
                textView.setText(R.string.restaurant_open);
            } else {
                textView.setTextAppearance(textView.getContext(), R.style.restaurant_closed);
                textView.setText(R.string.restaurant_closed);
            }
        }
    }

    public static void showRestaurantServiceType(Restaurant restaurant, TextView textView) {
        if (restaurant.getServiceTypeId()==null) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);

            int restaurantServices = restaurant.getServiceTypeId();
            ArrayList<String> serviceStrings = new ArrayList<String>();

            for (RestaurantServiceType type : RestaurantServiceType.values()) {
                if ((restaurantServices & type.getId()) == type.getId()) {
                    serviceStrings.add(textView.getContext().getString(type.getResourceId()));
                }
            }

            textView.setText(ArrayUtil.implode(serviceStrings, " - "));

//            RestaurantServiceType serviceType = RestaurantServiceType.getFromId(restaurant.getServiceTypeId());
//            if (serviceType != null) {
//                textView.setText(serviceType.getResourceId());
//            }
        }
    }

    public static String getStandardDeliveryTime(Restaurant restaurant) {
        if (restaurant.getMinDeliveryTime()>=60) {
            DecimalFormat decimalFormat = new DecimalFormat("0.#");
            float newMinDeliveryTime = (float)restaurant.getMinDeliveryTime() / 60f;
            float newMaxDeliveryTime = (float)restaurant.getMaxDeliveryTime() / 60f;
            if (newMaxDeliveryTime == newMaxDeliveryTime) {
                return String.format("%s hrs", decimalFormat.format(newMinDeliveryTime));
            } else {
                return String.format("%s - %s hrs", decimalFormat.format(newMinDeliveryTime),
                        decimalFormat.format(newMaxDeliveryTime));
            }
        } else {
            if (restaurant.getMinDeliveryTime() == restaurant.getMaxDeliveryTime()) {
                return String.format("%d min", restaurant.getMinDeliveryTime());
            } else {
                return String.format("%d - %d min", restaurant.getMinDeliveryTime(),
                        restaurant.getMaxDeliveryTime());
            }
        }
    }

    public static void displayRestaurantDescription(Restaurant restaurant, TextView textView) {
        textView.setText(restaurant.getDescription());
//        textView.setText(Html.fromHtml(restaurant.getDescription()));
    }

    public static void displayRestaurantPromotionValidity(
            RestaurantPromotion restaurantPromotion, TextView textView) {
        Context context = textView.getContext();

        SimpleDateFormat dateFormat = DateUtil.getUserMediumDateFormat(context);

        Date startDate = new Date(restaurantPromotion.getStartDate() * 1000);
        Date finishDate = new Date(restaurantPromotion.getFinishDate() * 1000);

//        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(context);

        textView.setText(context.getString(R.string.msg_from) + " " + dateFormat.format(startDate) +
                " " + context.getString(R.string.msg_to) + " " + dateFormat.format(finishDate));
    }

    public static void showRestaurantDishLike(RestaurantDish dish, ImageView imageView, TextView likeCount) {
        if (dish.getLiked()) {
            imageView.setImageResource(R.drawable.ic_action_like_active);
        } else {
            imageView.setImageResource(R.drawable.ic_action_like_inactive);
        }
        Context context = imageView.getContext();
        if (dish.getLikeCount() != null && dish.getLikeCount() > 0) {
            likeCount.setVisibility(View.VISIBLE);
            likeCount.setText(dish.getLikeCount().toString());
            if (dish.getLiked() != null && dish.getLiked()) {
                likeCount.setTextColor(context.getResources().getColor(R.color.apptheme_color_tenuous));
            } else {
                likeCount.setTextColor(context.getResources().getColor(R.color.default_text_data_value));
            }
        } else {
            likeCount.setVisibility(View.GONE);
        }
    }
}
