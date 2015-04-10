package com.jasoftsolutions.mikhuna.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantListFilterFragment;
import com.jasoftsolutions.mikhuna.activity.listener.ApplyActionListener;
import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.domain.RestaurantListFilter;
import com.jasoftsolutions.mikhuna.util.ContextUtil;

/**
 * Created by pc07 on 07/05/2014.
 */
public class FirstTimeActivity extends BaseActivity implements ApplyActionListener {

    private static final String TAG_FILTER_FRAGMENT = "tag_filter_fragment";

    private RestaurantListFilterPreferences restaurantListFilterPreferences;
    private RestaurantListFilterFragment restaurantListFilterFragment;

    private RestaurantListFilter currentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_time);

        final ImageView img = (ImageView) findViewById(R.id.img_new_functionality);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img.setVisibility(View.GONE);
            }
        });

        restaurantListFilterPreferences = new RestaurantListFilterPreferences(this);
        addRestaurantListFilterFragment(savedInstanceState);
    }

    private void addRestaurantListFilterFragment(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            restaurantListFilterFragment = new RestaurantListFilterFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, restaurantListFilterFragment, TAG_FILTER_FRAGMENT)
                    .commit();
            currentFilter = restaurantListFilterPreferences.loadFilter();
        } else {
            restaurantListFilterFragment = (RestaurantListFilterFragment)
                    getSupportFragmentManager().findFragmentByTag(TAG_FILTER_FRAGMENT);
            currentFilter = (RestaurantListFilter)
                    savedInstanceState.getSerializable(ArgKeys.RESTAURANT_LIST_FILTER);
        }
        restaurantListFilterFragment.setCurrentFilter(currentFilter);
        restaurantListFilterFragment.setApplyActionListener(this);
        restaurantListFilterFragment.setEmptyUbigeo(true);
//        restaurantListFilterFragment.setApplyButtonText(R.string.dialog_button_continue);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        restaurantListFilterFragment.computeFilter();
        outState.putSerializable(ArgKeys.RESTAURANT_LIST_FILTER, restaurantListFilterFragment.getCurrentFilter());
    }

    @Override
    public void onApplyAction(Object sender) {
        if (sender == restaurantListFilterFragment) {
            restaurantListFilterPreferences.saveFilter(restaurantListFilterFragment.getCurrentFilter());
            Intent restaurantListIntent = new Intent(this, RestaurantListActivity.class);
            startActivity(restaurantListIntent);
            ContextUtil.asyncSendGcmToBackend(FirstTimeActivity.this);
            finish();
        }
    }
}
