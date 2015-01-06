package com.jasoftsolutions.mikhuna.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.adapter.RestaurantDishListAdapter;
import com.jasoftsolutions.mikhuna.model.RestaurantDish;
import com.jasoftsolutions.mikhuna.store.RestaurantStore;
import com.jasoftsolutions.mikhuna.store.StoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantCarteDishesFragment extends Fragment {

    private static final String TAG = RestaurantCarteDishesFragment.class.getSimpleName();

    private ListView dishList;

    private Long dishCategoryServerId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurant_carte_dishes, container, false);

        if (savedInstanceState == null) {
            dishList = (ListView) rootView.findViewById(R.id.dish_list);
            refreshDishList();
        }

        return rootView;
    }

    private void refreshDishList() {
        if (dishList == null || dishCategoryServerId == null) {
            return;
        }
        RestaurantStore.getInstance().requestRestaurantDishCategoriesOf(dishCategoryServerId,
                new StoreAdapter() {
                    @Override
                    public void onSuccess(Object sender, Object data) {
                        data = getData();
                        final Object finalData = data;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dishList.setAdapter(new RestaurantDishListAdapter(getActivity(),
                                        (List<RestaurantDish>) finalData));
                            }
                        });
                    }
                });
    }

    public Long getDishCategoryServerId() {
        return dishCategoryServerId;
    }

    public void setDishCategoryServerId(Long dishCategoryServerId) {
        this.dishCategoryServerId = dishCategoryServerId;
        refreshDishList();
    }

    private static ArrayList<RestaurantDish> getData() {
        ArrayList<RestaurantDish> result = new ArrayList<RestaurantDish>();
        result.add(new RestaurantDish("Arroz con pollo", "Arroz verde con pollo y papa con crema",
                "S/. 8.00", true, 0l));
        result.add(new RestaurantDish("Lomo saltado", "El tradicional lomito saltado con arroz blanco",
                "S/. 10.00", true, 1l));
        result.add(new RestaurantDish("Sudado de pescado", null,
                "S/. 10.50", false, 0l));
        result.add(new RestaurantDish("Pollo a la piña", "Arroz chaufa con pollo y rodajas de piña",
                "S/. 8.00", false, 3l));
        result.add(new RestaurantDish("Ají de gallina", null,
                "S/. 7.00", true, 5l));
        result.add(new RestaurantDish("Locro con bistek", null,
                "S/. 9.50", true, 0l));

        return result;
    }
}
