package com.jasoftsolutions.mikhuna.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.util.RestaurantViewUtil;
import com.jasoftsolutions.mikhuna.model.RestaurantPromotion;

import java.util.ArrayList;

public class RestaurantPromotionListReducedAdapter extends ArrayAdapter<RestaurantPromotion> {

	private ArrayList<RestaurantPromotion> data;

	public RestaurantPromotionListReducedAdapter(Context context,
                                                 ArrayList<RestaurantPromotion> objects) {
		super(context, R.layout.reduced_restaurant_promotion_listitem, objects);
		data=objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        RestaurantPromotion restaurantPromotion=data.get(position);

        View listItem = null;
        ViewHolder holder = null;

        if (convertView==null) {
		    LayoutInflater inflater=LayoutInflater.from(getContext());
            listItem = inflater.inflate(R.layout.reduced_restaurant_promotion_listitem, null);

            holder=new ViewHolder();
            holder.logoImageView = (ImageView)listItem.findViewById(R.id.restaurant_promotion_listitem_logo);
            holder.restaurantNameTextView = (TextView)listItem.findViewById(R.id.restaurant_promotion_listitem_restaurant_name);
            holder.titleTextView = (TextView)listItem.findViewById(R.id.restaurant_promotion_listitem_title);
            holder.descriptionTextView = (TextView)listItem.findViewById(R.id.restaurant_promotion_listitem_description);
            holder.validityTextView = (TextView)listItem.findViewById(R.id.restaurant_promotion_listitem_validity);

            listItem.setTag(R.id.tag_view_holder, holder);
        } else {
            listItem = convertView;
            holder = (ViewHolder)listItem.getTag(R.id.tag_view_holder);
        }

        RestaurantViewUtil.displaySmallLogo(restaurantPromotion.getRestaurant(), holder.logoImageView);
        holder.restaurantNameTextView.setText(restaurantPromotion.getRestaurant().getName());
        holder.titleTextView.setText(restaurantPromotion.getTitle());
        holder.descriptionTextView.setText(restaurantPromotion.getDescription());
        RestaurantViewUtil.displayRestaurantPromotionValidity(restaurantPromotion, holder.validityTextView);

        return listItem;
	}

    private static class ViewHolder {
        public TextView restaurantNameTextView;
        public TextView titleTextView;
        public TextView descriptionTextView;
        public TextView validityTextView;
        public ImageView logoImageView;
    }
}
