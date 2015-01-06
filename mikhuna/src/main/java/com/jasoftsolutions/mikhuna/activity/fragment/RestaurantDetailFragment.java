package com.jasoftsolutions.mikhuna.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.Tags;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantDetailFragment extends Fragment {

    private static final String TAG = RestaurantDetailFragment.class.getSimpleName();

    private Restaurant restaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

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
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            RestaurantDetailHeaderFragment rdhf = (RestaurantDetailHeaderFragment) fm.findFragmentByTag(Tags.FRAGMENT_HEADER);
            if (rdhf == null) {
                rdhf = new RestaurantDetailHeaderFragment();
                ft.add(R.id.frame_restaurant_header, rdhf, Tags.FRAGMENT_HEADER);
            }

            RestaurantInformationFragment rif = (RestaurantInformationFragment) fm.findFragmentByTag(Tags.FRAGMENT_INFORMATION);
            if (rif == null) {
                rif = new RestaurantInformationFragment();
                ft.add(R.id.frame_restaurant_info, rif, Tags.FRAGMENT_INFORMATION);
            }

            RestaurantActionsFragment raf = (RestaurantActionsFragment) fm.findFragmentByTag(Tags.FRAGMENT_ACTIONS);
            if (raf == null) {
                raf = new RestaurantActionsFragment();
                ft.add(R.id.frame_restaurant_actions, raf, Tags.FRAGMENT_ACTIONS);
            }

            ft.commit();

            rdhf.display(restaurant);
            rif.display(restaurant);
            raf.display(restaurant);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

}
