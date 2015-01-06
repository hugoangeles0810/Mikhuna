package com.jasoftsolutions.mikhuna.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.ui.CustomViewPager;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantCarteFragment extends Fragment
    implements RestaurantCarteCategoriesFragment.Listener {

    private static final String TAG = RestaurantCarteFragment.class.getSimpleName();

    private View rootView;
    private CustomViewPager viewPager;
    private RestaurantCarteCategoriesFragment restaurantCarteCategoriesFragment;
    private RestaurantCarteDishesFragment restaurantCarteDishesFragment;

    private Long restaurantServerId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_restaurant_carte, container, false);

        if (savedInstanceState == null) {
            checkExtraData(getArguments());
            initializeFragments();
        }

        return rootView;
    }

    private void checkExtraData(Bundle data) {

    }

    private void initializeFragments() {
        if (rootView == null) return;

        viewPager = (CustomViewPager) rootView.findViewById(R.id.view_pager);

        restaurantCarteCategoriesFragment = new RestaurantCarteCategoriesFragment();
        restaurantCarteDishesFragment = new RestaurantCarteDishesFragment();

        viewPager.setAdapter(new CartePagerAdapter(getChildFragmentManager()));
        viewPager.setSwippingEnabled(false);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                viewPager.setSwippingEnabled(true);
            }
        });
    }

    public boolean isCategoryViewVisible() {
        return viewPager.getCurrentItem() == CartePagerAdapter.CATEGORY_POSITION;
    }

    public void setCartePageCategory() {
        viewPager.setCurrentItem(CartePagerAdapter.CATEGORY_POSITION);
    }

    public Long getRestaurantServerId() {
        return restaurantServerId;
    }

    public void setRestaurantServerId(Long restaurantServerId) {
        this.restaurantServerId = restaurantServerId;
        restaurantCarteCategoriesFragment.setRestaurantServerId(restaurantServerId);
    }

    @Override
    public void onSelectDishCategory(Long dishCategoryServerId) {
        restaurantCarteDishesFragment.setDishCategoryServerId(dishCategoryServerId);
        viewPager.setCurrentItem(1);
    }



    private class CartePagerAdapter extends FragmentPagerAdapter {

        private final static int CATEGORY_POSITION = 0;
        private final static int PLATE_POSITION = 1;

        public CartePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (CATEGORY_POSITION == i) {
                return restaurantCarteCategoriesFragment;
            } else if (PLATE_POSITION == i) {
                return restaurantCarteDishesFragment;
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

}
