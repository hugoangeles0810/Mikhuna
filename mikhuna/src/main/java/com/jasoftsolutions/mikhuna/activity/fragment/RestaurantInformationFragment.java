package com.jasoftsolutions.mikhuna.activity.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.util.RestaurantViewUtil;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.domain.Weekday;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.DateUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantInformationFragment extends Fragment {

    private static final String TAG = RestaurantInformationFragment.class.getSimpleName();

    private Restaurant restaurant;

    private LayoutInflater layoutInflater;
    private ViewGroup viewGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        layoutInflater = inflater;
        viewGroup = container;

        View rootView = inflater.inflate(R.layout.fragment_restaurant_information, container, false);

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
            if (rootView != null) {
                Restaurant r = restaurant;
                if (r == null) {
                    r = new Restaurant();   // si es nulo, mostrar datos vacíos
                }

                RestaurantManager rm = new RestaurantManager();

                TextView descriptionTextView=(TextView)rootView.findViewById(R.id.restaurant_detail_description);
                RestaurantViewUtil.displayRestaurantDescription(r, descriptionTextView);

                View estimatedArrivalTimeLayout = rootView.findViewById(R.id.restaurant_detail_estimated_arrival_time_layout);
                if (restaurant.isDelivery()) {
                    estimatedArrivalTimeLayout.setVisibility(View.VISIBLE);
                    TextView estimatedArrivalTimeTextView=(TextView)rootView.findViewById(R.id.restaurant_detail_estimated_arrival_time);
                    estimatedArrivalTimeTextView.setText(RestaurantViewUtil.getStandardDeliveryTime(r));
                } else {
                    estimatedArrivalTimeLayout.setVisibility(View.GONE);
                }

                TextView addressTextView=(TextView)rootView.findViewById(R.id.restaurant_detail_address);
                LinearLayout addressSegment = (LinearLayout) rootView.findViewById(R.id.restaurant_address_segment);
                if (restaurant.getAddress() != null && restaurant.getAddress().trim().length() > 0) {
                    addressSegment.setVisibility(View.VISIBLE);
                    addressTextView.setText(r.getAddress());
                } else {
                    addressSegment.setVisibility(View.GONE);
                }

                TextView isOpenTextView=(TextView)rootView.findViewById(R.id.restaurant_detail_is_open);
                RestaurantViewUtil.showRestaurantIsOpen(r, isOpenTextView);

                LinearLayout timetableRows = (LinearLayout)rootView.findViewById(R.id.restaurant_timetable_rows);
                timetableRows.removeAllViews();

                LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                if (r.getTimetableDescription() != null
                        && r.getTimetableDescription().trim().length() > 0) {
                    TextView tv = new TextView(getActivity());
                    tv.setText(r.getTimetableDescription());
                    timetableRows.addView(tv, linearLayoutParams);
                } else {
                    Map<Integer, List<String[]>> rtt = rm.getParsedTimetableFor(r);

                    final int currentWeekday = DateUtil.getCurrentWeekday().getId();

                    for (int weekday : rtt.keySet()) {
                        View v = layoutInflater.inflate(R.layout.fragment_restaurant_timetable_day, viewGroup, false);

                        TextView weekdayTextView = (TextView) v.findViewById(R.id.weekday_text);

                        String weekdayStr = getString(Weekday.getFromId(weekday).getResourceId());
                        if (currentWeekday == weekday) {
                            Editable editable = Editable.Factory.getInstance().newEditable(weekdayStr);
                            editable.setSpan(new StyleSpan(Typeface.BOLD),
                                    0, weekdayStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            weekdayTextView.setText(editable);
                        } else {
                            weekdayTextView.setText(weekdayStr);
                        }

                        GridLayout gl = (GridLayout) v.findViewById(R.id.weekday_timetable);
                        for (String[] hour : rtt.get(weekday)) {
                            String hourStr = hour[0] + " - " + hour[1];
                            TextView hourView = new TextView(this.getActivity());
                            hourView.setPadding(0, 0, 15, 0);
                            hourView.setText(hourStr);

                            gl.addView(hourView);
                        }

                        timetableRows.addView(v, linearLayoutParams);
                    }
                }

            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

}
