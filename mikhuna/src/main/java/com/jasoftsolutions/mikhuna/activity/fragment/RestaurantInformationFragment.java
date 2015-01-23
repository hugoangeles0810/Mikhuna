package com.jasoftsolutions.mikhuna.activity.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.util.RestaurantViewUtil;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.domain.Weekday;
import com.jasoftsolutions.mikhuna.model.Link;
import com.jasoftsolutions.mikhuna.model.Pay;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.Service;
import com.jasoftsolutions.mikhuna.util.DateUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.ResourcesUtil;
import com.jasoftsolutions.mikhuna.util.StringUtil;

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

                LinearLayout containerPayments = (LinearLayout) rootView.findViewById(R.id.container_payments_methods);
                if (restaurant.getPayMethods() != null && !restaurant.getPayMethods().isEmpty()){
                    containerPayments.setVisibility(View.VISIBLE);
                    LinearLayout layoutPayments = (LinearLayout) rootView.findViewById(R.id.list_img_payments_methods);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    float den = rootView.getContext().getResources().getDisplayMetrics().density;
                    lp.height=(int)(40*den);
                    lp.width=(int)(45*den);
//                    lp.setMargins(left, top, right, bottom);
                    for (Pay p : restaurant.getPayMethods()){
                        ImageView img = new ImageView(rootView.getContext());
                        Drawable d = ResourcesUtil.getDrawableByName(rootView.getContext(), p.getNameFile());
                        img.setImageDrawable(d);
                        img.setLayoutParams(lp);
                        layoutPayments.addView(img);
                    }
                }else{
                    containerPayments.setVisibility(View.GONE);
                }

                LinearLayout containerNetworks = (LinearLayout) rootView.findViewById(R.id.container_networks);
                if (restaurant.getLinks() != null && !restaurant.getLinks().isEmpty()){
                    containerNetworks.setVisibility(View.VISIBLE);
                    LinearLayout layoutNetworks = (LinearLayout) rootView.findViewById(R.id.list_img_networks);

                    for (final Link l : restaurant.getLinks()){
                        ImageView img = new ImageView(rootView.getContext());
                        Drawable d = getIconOfLink(l.getTypeLink());
                        img.setImageDrawable(d);
                        layoutNetworks.addView(img);
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(l.getLink()));
                                startActivity(i);
                            }
                        });
                    }
                }else{
                    containerNetworks.setVisibility(View.GONE);
                }

                LinearLayout containerServices = (LinearLayout) rootView.findViewById(R.id.container_services);
                if (restaurant.getServices() != null && !restaurant.getServices().isEmpty()){
                    containerServices.setVisibility(View.VISIBLE);
                    LinearLayout layoutServices = (LinearLayout) rootView.findViewById(R.id.list_tv_services);
                    TextView textView = new TextView(rootView.getContext());
                    textView.setText(StringUtil.getServicesOfRestaurant(restaurant.getServices()));
                    layoutServices.addView(textView);

                }else{
                    containerServices.setVisibility(View.GONE);
                }


            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    public Drawable getIconOfLink(Integer typeLink){
        Drawable d;

        switch (typeLink){
            case 1:
                d = getResources().getDrawable(R.drawable.website);
                break;
            case 2:
                d = getResources().getDrawable(R.drawable.facebook1);
                break;
            case 3:
                d = getResources().getDrawable(R.drawable.twitter1);
                break;
            case 4:
                d = getResources().getDrawable(R.drawable.email);
                break;
            case 5:
                d = getResources().getDrawable(R.drawable.instagram);
                break;
            case 6:
                d = getResources().getDrawable(R.drawable.youtube1);
                break;
            default:
                d = getResources().getDrawable(R.drawable.website);
                break;

        }
        return d;
    }

}
