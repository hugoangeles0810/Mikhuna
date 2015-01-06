package com.jasoftsolutions.mikhuna.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.listener.CallButtonListener;
import com.jasoftsolutions.mikhuna.activity.listener.CarteButtonActionListener;
import com.jasoftsolutions.mikhuna.activity.listener.MapsButtonListener;
import com.jasoftsolutions.mikhuna.activity.listener.RetryActionListener;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantActionsFragment extends Fragment {

    private static final String TAG = RestaurantActionsFragment.class.getSimpleName();

    private RetryActionListener retryActionListener;

    private Restaurant restaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurant_actions, container, false);

        if (savedInstanceState == null) {
            render(rootView);
        }

        return rootView;
    }

    public void display(Restaurant r) {
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
                ImageView callButton=(ImageView)rootView.findViewById(R.id.restaurant_call_action);
                if (callButton.getTag(R.id.tag_restaurant) == null) {
                    callButton.setTag(R.id.tag_restaurant, restaurant);
                    callButton.setOnClickListener(new CallButtonListener());
                }

                ImageView mapsButton=(ImageView)rootView.findViewById(R.id.restaurant_map_action);
                if ((restaurant.getLatitude() == null || restaurant.getLatitude() == 0)
                        && (restaurant.getLongitude() == null || restaurant.getLongitude() == 0)) {
                    mapsButton.setVisibility(View.GONE);
                } else {
                    mapsButton.setVisibility(View.VISIBLE);
                    if (mapsButton.getTag(R.id.tag_restaurant) == null) {
                        mapsButton.setTag(R.id.tag_restaurant, restaurant);
                        mapsButton.setOnClickListener(new MapsButtonListener());
                    }
                }

                ImageView carteButton=(ImageView)rootView.findViewById(R.id.restaurant_carte_action);
                if (carteButton.getTag(R.id.tag_restaurant_server_id) == null) {
                    carteButton.setTag(R.id.tag_restaurant_server_id, restaurant.getServerId());
                    carteButton.setOnClickListener(new CarteButtonActionListener());
                }

                if (restaurant.getNumberProductCategory() <= 0){ carteButton.setVisibility(View.GONE); }
            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

}
