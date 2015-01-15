package com.jasoftsolutions.mikhuna.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.adapter.DishCategoryAdapter;
import com.jasoftsolutions.mikhuna.model.RestaurantDishCategory;

import java.util.ArrayList;

/**
 * Created by Hugo on 10/01/2015.
 */
public class RestaurantDishCategoryFragment extends Fragment {

    private ArrayList<RestaurantDishCategory> restaurantDishCategories;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_restaurant_carte_categories, container, false);

        if (savedInstanceState == null ){
            render(rootView);
        }
        return rootView;
    }

    public void display(ArrayList<RestaurantDishCategory> dishCategories){
        this.restaurantDishCategories = dishCategories;
        refresh();
    }

    public void refresh() {
        View rootView = getView();
        if (rootView != null) {
            render(rootView);
        }
    }

    public void render(View rootView) {
        ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.category_list);
        listView.setAdapter(new DishCategoryAdapter(getActivity(), restaurantDishCategories));
    }
}
