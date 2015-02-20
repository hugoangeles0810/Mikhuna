package com.jasoftsolutions.mikhuna.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantListFilterFragment;
import com.jasoftsolutions.mikhuna.activity.listener.ApplyActionListener;
import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

public class FilterMapActivity extends ActionBarActivity implements
        ApplyActionListener {

    public static String TAG = FilterMapActivity.class.getSimpleName();

    private RestaurantListFilterFragment fragmentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_map);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);

        render();

    }

    private void render() {
        FragmentManager fm = getSupportFragmentManager();

        fragmentFilter = (RestaurantListFilterFragment)
                fm.findFragmentByTag(RestaurantListFilterFragment.TAG);

        if (fragmentFilter == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragmentFilter = RestaurantListFilterFragment.newInstance(true);

            ft.add(R.id.container_filter_map, fragmentFilter, RestaurantListFilterFragment.TAG);

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }

        fragmentFilter.setApplyActionListener(this);
        RestaurantListFilterPreferences preferences =
                new RestaurantListFilterPreferences(this, RestaurantListFilterPreferences.PREF_MAP);
        fragmentFilter.setCurrentFilter(preferences.loadFilter());
    }

    @Override
    public void onApplyAction(Object sender) {
        if (sender != null) {
            try {
                RestaurantListFilterFragment fragment =
                        (RestaurantListFilterFragment) sender;
                RestaurantListFilterPreferences preferences = new RestaurantListFilterPreferences(
                        this, RestaurantListFilterPreferences.PREF_MAP);
                preferences.saveFilter(fragment.getCurrentFilter());
                finish();
            } catch (ClassCastException e) {
                ExceptionUtil.ignoreException(e);
            }
        }
    }

    public static Intent getLauncherIntent(Context context) {
        return new Intent(context, FilterMapActivity.class);
    }
}
