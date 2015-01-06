package com.jasoftsolutions.mikhuna.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jasoftsolutions.mikhuna.R;

/**
 * Created by pc07 on 23/04/2014.
 */
public class FoodTypePreferencesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preferences_foodtype, container, false);

        ListView listView=(ListView)rootView.findViewById(R.id.preferences_foodtype_list);

        String[] data = getResources().getStringArray(R.array.preferences_foodtype_test_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, data);

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (0 == i) { // Preferencias
//                    Intent preferencesIntent = new Intent(getActivity(), PreferencesActivity.class);
//                    startActivity(preferencesIntent);
//                }
//            }
//        });

        return rootView;
    }
}
