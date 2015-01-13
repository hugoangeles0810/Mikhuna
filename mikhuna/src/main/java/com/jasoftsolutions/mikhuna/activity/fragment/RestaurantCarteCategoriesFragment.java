package com.jasoftsolutions.mikhuna.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.model.RestaurantDishCategory;
import com.jasoftsolutions.mikhuna.store.RestaurantStore;
import com.jasoftsolutions.mikhuna.store.StoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantCarteCategoriesFragment extends Fragment {

    private static final String TAG = RestaurantCarteCategoriesFragment.class.getSimpleName();

    private ListView categoryList;
    private Long restaurantServerId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurant_carte_categories, container, false);

        if (savedInstanceState == null) {
            categoryList = (ListView) rootView.findViewById(R.id.category_list);

            categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FragmentActivity fa = getActivity();
                    if (fa instanceof Listener) {
                        RestaurantDishCategory rdc = (RestaurantDishCategory)
                                categoryList.getAdapter().getItem(position);
                        ((Listener) fa).onSelectDishCategory(rdc.getServerId());
                    }
                }
            });

            refreshCategoryList();
        }

        return rootView;
    }

    public Long getRestaurantServerId() {
        return restaurantServerId;
    }

    public void setRestaurantServerId(Long restaurantServerId) {
        this.restaurantServerId = restaurantServerId;
        refreshCategoryList();
    }

    private void refreshCategoryList() {
        if (restaurantServerId == null || categoryList == null) {
            return;
        }
//        RestaurantStore.getInstance().requestRestaurantDishCategoriesOf(restaurantServerId,
//                new StoreAdapter() {
//                    @Override
//                    public void onSuccess(Object sender, Object data) {
//                        data = getData();
//                        categoryList.setAdapter(new ArrayAdapter<RestaurantDishCategory>(getActivity(),
//                                android.R.layout.simple_list_item_activated_1, (List<RestaurantDishCategory>)data));
//                    }
//                });
    }

    private static ArrayList<RestaurantDishCategory> getData() {
        ArrayList<RestaurantDishCategory> result = new ArrayList<RestaurantDishCategory>();
        result.add(new RestaurantDishCategory(1l, 1l, "Menu"));
        result.add(new RestaurantDishCategory(1l, 1l, "Bebidas"));
        result.add(new RestaurantDishCategory(1l, 1l, "Postres"));

        return result;
    }

    public static interface Listener {
        void onSelectDishCategory(Long dishCategoryServerId);
    }

}
