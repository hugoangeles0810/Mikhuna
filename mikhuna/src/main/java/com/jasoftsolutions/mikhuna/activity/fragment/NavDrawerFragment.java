package com.jasoftsolutions.mikhuna.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.PreferencesActivity;

/**
 * Created by pc07 on 23/04/2014.
 */
public class NavDrawerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navdrawer, container, false);

        ListView listView=(ListView)rootView.findViewById(R.id.main_navdraw_option_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (0 == i) { // Salir
                    Intent pref = new Intent(getActivity(), PreferencesActivity.class);
                    startActivity(pref);
                } else if (1 == i) { // Salir
                    System.exit(0);
                }
            }
        });

        return rootView;
    }
}
