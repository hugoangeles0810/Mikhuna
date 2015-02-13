package com.jasoftsolutions.mikhuna.activity.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.Tags;
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantListFilterFragment;
import com.jasoftsolutions.mikhuna.activity.listener.ApplyActionListener;
import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;

/**
 * Created by Hugo on 09/02/2015.
 */
public class MapFilterDialog extends DialogFragment {

    private ApplyActionListener listener;

    public static MapFilterDialog newInstance(){
        MapFilterDialog rf = new MapFilterDialog();
        return  rf;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.FilterDialogCustom);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_dialog, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        RestaurantListFilterFragment fragment = (RestaurantListFilterFragment)
                fm.findFragmentByTag(Tags.FRAGMENT_FILTER_MAP);
        if (fragment == null){
            fragment = RestaurantListFilterFragment.newInstance(true);
            fragment.setApplyActionListener(listener);
            fragment.setCurrentFilter(new RestaurantListFilterPreferences(
                    getActivity(), RestaurantListFilterPreferences.PREF_MAP).loadFilter());
            ft.add(R.id.container, fragment, Tags.FRAGMENT_FILTER_MAP);
        }

        ft.commit();
    }

    public ApplyActionListener getListener() {
        return listener;
    }

    public void setListener(ApplyActionListener listener) {
        this.listener = listener;
    }
}
