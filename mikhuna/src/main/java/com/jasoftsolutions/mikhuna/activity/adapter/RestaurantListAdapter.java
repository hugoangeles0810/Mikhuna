package com.jasoftsolutions.mikhuna.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.listener.CallButtonListener;
import com.jasoftsolutions.mikhuna.activity.util.RestaurantViewUtil;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.StringUtil;

import java.util.List;

public class RestaurantListAdapter extends ArrayAdapter<Restaurant> {

	private List<Restaurant> data;
    private CallButtonListener callButtonListener;

    private RestaurantManager rm;
	
	public RestaurantListAdapter(Context context,
			List<Restaurant> objects) {
		super(context, R.layout.restaurant_listitem, objects);
		data=objects;
        callButtonListener = new CallButtonListener();
        rm = new RestaurantManager();
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        Restaurant restaurant=data.get(position);
        rm.setRestaurantOpenOrClosed(restaurant);

        View listItem=null;
        ViewHolder holder=null;

        if (convertView==null) {
		    LayoutInflater inflater=LayoutInflater.from(getContext());
            listItem = inflater.inflate(R.layout.restaurant_listitem, null);

            holder=new ViewHolder();
            holder.titleTextView=(TextView)listItem.findViewById(R.id.restaurant_list_item_title);
            holder.descriptionTextView=(TextView)listItem.findViewById(R.id.restaurant_list_item_description);
            holder.minAmountTextViewLayout=listItem.findViewById(R.id.restaurant_list_item_min_amount_layout);
            holder.minAmountTextView=(TextView)listItem.findViewById(R.id.restaurant_list_item_min_amount);
//            holder.phoneTextView=(TextView)listItem.findViewById(R.id.restaurant_list_item_phone);
            holder.logoImageView=(ImageView)listItem.findViewById(R.id.restaurant_list_item_logo);
            holder.ratingBar=(RatingBar)listItem.findViewById(R.id.restaurant_list_item_rating);
//            holder.callButtonImageView=(ImageView)listItem.findViewById(R.id.restaurant_list_item_call_action);
            holder.estimatedArrivalTimeTextViewLayout=listItem.findViewById(R.id.restaurant_list_item_estimated_arrival_time_layout);
            holder.estimatedArrivalTimeTextView=(TextView)listItem.findViewById(R.id.restaurant_list_item_estimated_arrival_time);
            holder.isOpenTextView = (TextView)listItem.findViewById(R.id.restaurant_list_item_is_open);
            holder.serviceTypeTextView = (TextView)listItem.findViewById(R.id.restaurant_list_item_service_type);

            // Sólo es necesario una vez
//            holder.callButtonImageView.setOnClickListener(callButtonListener);

//            listItem.setTag(R.id.tag_restaurant, restaurant);
            listItem.setTag(R.id.tag_view_holder, holder);
        } else {
            listItem=convertView;
            holder=(ViewHolder)listItem.getTag(R.id.tag_view_holder);
        }

        holder.titleTextView.setText(restaurant.getName());
        RestaurantViewUtil.displayRestaurantDescription(restaurant, holder.descriptionTextView);

        float minAmount=0;
        if (restaurant.getMinAmount()!=null) {
            minAmount=restaurant.getMinAmount();
        }
        if (restaurant.isDelivery()) {
            holder.estimatedArrivalTimeTextViewLayout.setVisibility(View.VISIBLE);
            holder.minAmountTextViewLayout.setVisibility(View.VISIBLE);
            holder.minAmountTextView.setText(StringUtil.currencyFormat(restaurant.getCurrency(), minAmount));
            holder.estimatedArrivalTimeTextView.setText(RestaurantViewUtil.getStandardDeliveryTime(restaurant));
        } else {
            holder.estimatedArrivalTimeTextViewLayout.setVisibility(View.GONE);
            holder.minAmountTextViewLayout.setVisibility(View.GONE);
        }

        RestaurantViewUtil.displaySmallLogo(restaurant, holder.logoImageView);
        RestaurantViewUtil.setRating(restaurant, holder.ratingBar);

        RestaurantViewUtil.showRestaurantIsOpen(restaurant, holder.isOpenTextView);
        RestaurantViewUtil.showRestaurantServiceType(restaurant, holder.serviceTypeTextView);

        return listItem;
	}

    private static class ViewHolder {
        public TextView titleTextView;
        public TextView descriptionTextView;
        public ImageView logoImageView;
        public RatingBar ratingBar;
        public View minAmountTextViewLayout;
        public TextView minAmountTextView;
        public TextView estimatedArrivalTimeTextView;
        public View estimatedArrivalTimeTextViewLayout;

        public TextView isOpenTextView;
        public TextView serviceTypeTextView;

    }
}
