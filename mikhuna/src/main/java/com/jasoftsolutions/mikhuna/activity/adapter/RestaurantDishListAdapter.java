package com.jasoftsolutions.mikhuna.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.listener.LikeButtonListener;
import com.jasoftsolutions.mikhuna.activity.util.RestaurantViewUtil;
import com.jasoftsolutions.mikhuna.model.RestaurantDish;

import java.util.List;

public class RestaurantDishListAdapter extends ArrayAdapter<RestaurantDish> {

	private List<RestaurantDish> data;
    private LikeButtonListener likeButtonListener = new LikeButtonListener();

	public RestaurantDishListAdapter(Context context,
                                     List<RestaurantDish> objects) {
		super(context, R.layout.restaurant_carte_dish_listitem, objects);
		data=objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        RestaurantDish dish=data.get(position);

        ViewHolder holder=null;

        if (convertView == null) {
		    LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.restaurant_carte_dish_listitem, null);

            holder=new ViewHolder();
            holder.dishText = (TextView) convertView.findViewById(R.id.dish_text);
            holder.dishSecondaryText = (TextView) convertView.findViewById(R.id.dish_secondary_text);
            holder.likeActionImage = (ImageView) convertView.findViewById(R.id.dish_like_action);
                holder.likeActionImage.setOnClickListener(likeButtonListener);
            holder.likeCountText = (TextView) convertView.findViewById(R.id.dish_like_count);
            holder.priceText = (TextView) convertView.findViewById(R.id.dish_price);

            convertView.setTag(R.id.tag_view_holder, holder);
        } else {
            holder=(ViewHolder)convertView.getTag(R.id.tag_view_holder);
        }

        holder.dishText.setText(dish.getName());
        if (dish.getDescription() != null && dish.getDescription().trim().length() > 0) {
            holder.dishSecondaryText.setVisibility(View.VISIBLE);
            holder.dishSecondaryText.setText(dish.getDescription());
        } else {
            holder.dishSecondaryText.setVisibility(View.GONE);
        }
        RestaurantViewUtil.showRestaurantDishLike(dish, holder.likeActionImage, holder.likeCountText);

        holder.priceText.setText(dish.getPriceText());

        holder.likeActionImage.setTag(R.id.tag_restaurant_dish, dish);
        holder.likeActionImage.setTag(R.id.tag_restaurant_dish_like_count_text, holder.likeCountText);

        return convertView;
	}

    private static class ViewHolder {
        public TextView dishText;
        public TextView dishSecondaryText;
        public ImageView likeActionImage;
        public TextView likeCountText;
        public TextView priceText;
    }
}
