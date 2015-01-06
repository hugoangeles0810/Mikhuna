package com.jasoftsolutions.mikhuna.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.util.RestaurantViewUtil;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.StringUtil;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantDetailHeaderFragment extends Fragment {

    private static final String TAG = RestaurantDetailHeaderFragment.class.getSimpleName();

    private Restaurant restaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurant_detail_header, container, false);

        if (savedInstanceState == null) {
            render(rootView);
        }

        return rootView;
    }

    public void display(Restaurant r) {
        Log.i(TAG, "set restaurant");
        restaurant = r;
        refresh();
    }

    public void refresh() {
        View rootView = getView();
        if (rootView != null) {
            render(rootView);
        }
    }

    private void render(View rootView) {
        try {
            if (rootView != null) {
                Restaurant r = restaurant;
                if (r == null) {
                    r = new Restaurant();   // si es nulo, mostrar datos vacíos
                }

                ImageView logo=(ImageView)rootView.findViewById(R.id.restaurant_detail_logo);
                RestaurantViewUtil.displaySmallLogo(r, logo);

                TextView titleTextView=(TextView)rootView.findViewById(R.id.restaurant_detail_title);
                titleTextView.setText(r.getName());

                RatingBar ratingBar=(RatingBar)rootView.findViewById(R.id.restaurant_detail_rating);
                RestaurantViewUtil.setRating(r, ratingBar);

                View minAmountAndDeliveryLayout = rootView.findViewById(R.id.restaurant_minamount_and_delivery_layout);
                if (restaurant.isDelivery()) {
                    minAmountAndDeliveryLayout.setVisibility(View.VISIBLE);

                    TextView minAmountTextView=(TextView)rootView.findViewById(R.id.restaurant_detail_min_amount);
                    float minAmount=0;
                    if (r.getMinAmount()!=null) {
                        minAmount = r.getMinAmount();
                    }
                    minAmountTextView.setText(StringUtil.currencyFormat(r.getCurrency(), minAmount));

                    TextView shippingCostTextView=(TextView)rootView.findViewById(R.id.restaurant_detail_shipping_cost);
                    if (r.getShippingCost()!=null) {
                        shippingCostTextView.setText(StringUtil.currencyFormat(r.getCurrency(), r.getShippingCost()));
                    } else {
                        shippingCostTextView.setText(StringUtil.currencyFormat(r.getCurrency(), 0));
                    }
                } else {
                    minAmountAndDeliveryLayout.setVisibility(View.GONE);
                }

                TextView serviceTypeTextView=(TextView)rootView.findViewById(R.id.restaurant_detail_service_type);
                RestaurantViewUtil.showRestaurantServiceType(r, serviceTypeTextView);
            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

}
