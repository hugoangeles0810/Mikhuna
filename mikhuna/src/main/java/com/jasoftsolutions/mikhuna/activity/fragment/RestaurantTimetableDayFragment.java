package com.jasoftsolutions.mikhuna.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.domain.Weekday;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

import java.util.List;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantTimetableDayFragment extends Fragment {

    private static final String TAG = RestaurantTimetableDayFragment.class.getSimpleName();

    private int weekday;
    private List<String[]> timetable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurant_timetable_day, container, false);

        if (savedInstanceState == null) {
            render(rootView);
        }

        return rootView;
    }


    public void display(int w, List<String[]> tt) {
        weekday = w;
        timetable = tt;
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
                if (weekday == 0) {
                    return;
                }

                TextView weekdayText = (TextView) rootView.findViewById(R.id.weekday_text);
                weekdayText.setText(getString(Weekday.getFromId(weekday).getResourceId()));

                GridLayout gl = (GridLayout) rootView.findViewById(R.id.weekday_timetable);
                for (String[] hour : timetable) {
                    String hourStr = hour[0] + " - " + hour[1];
                    TextView hourView = new TextView(this.getActivity());
                    hourView.setText(hourStr);

                    gl.addView(hourView);
                }

//                Map<Integer, List<String[]>> rtt = rm.getParsedTimetableFor(r);
//
//                LinearLayout timetableLayout = (LinearLayout)rootView.findViewById(R.id.restaurant_detail_timetable_layout);
//                if (r.getLastUpdate() != null && r.getLastUpdate() > 0) {
//                    String timeTableWeekdayStr="-", timeTableTimeStr="";
//                    int weekdayTextViewBoldStart=0, weekdayTextViewBoldEnd=0;
//                    if (r.getRestaurantTimetables()!=null && r.getRestaurantTimetables().size()>0) {
//                        timeTableWeekdayStr=""; timeTableTimeStr="";
//                        Weekday today = DateUtil.getCurrentWeekday();
//                        Context context = getActivity();
//                        // tomar el primer día (se sabe que tiene al menos 1 debido a la condición previa)
//                        //                int lastDay = restaurant.getRestaurantTimetables().get(0).getWeekday();
//                        int lastDay = 0;
//                        for (RestaurantTimetable timetable : r.getRestaurantTimetables()) {
//                            String timeStrToAdd = timetable.getStartTime() + " - " + timetable.getFinishTime();
//                            if (lastDay != timetable.getWeekday()) {
//                                String dayToAdd = context.getString(Weekday.getFromId(timetable.getWeekday()).getResourceId()) + ":\n";
//                                if (timetable.getWeekday() == today.getId()) {
//                                    weekdayTextViewBoldStart = timeTableWeekdayStr.length();
//                                    weekdayTextViewBoldEnd = weekdayTextViewBoldStart + dayToAdd.length();
//                                }
//                                timeTableWeekdayStr += dayToAdd;
//                                timeTableTimeStr += "\n" + timeStrToAdd;
//                            } else {
//                                timeTableTimeStr += "      " + timeStrToAdd;
//                            }
//                            lastDay = timetable.getWeekday();
//                        }
//                        // omitir el primer espacio
//                        timeTableTimeStr = timeTableTimeStr.substring(1);
//                    }
//                    TextView timetableWeekdayTextView=(TextView)rootView.findViewById(R.id.restaurant_detail_timetable_weekday);
//                    if (weekdayTextViewBoldStart != 0 || weekdayTextViewBoldEnd != 0) {
//                        Editable editable = Editable.Factory.getInstance().newEditable(timeTableWeekdayStr);
//                        editable.setSpan(new StyleSpan(Typeface.BOLD),
//                                weekdayTextViewBoldStart, weekdayTextViewBoldEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        timetableWeekdayTextView.setText(editable);
//                    } else {
//                        timetableWeekdayTextView.setText(timeTableWeekdayStr);
//                    }
//                    TextView timetableTimeTextView=(TextView)rootView.findViewById(R.id.restaurant_detail_timetable_time);
//                    timetableTimeTextView.setText(timeTableTimeStr);
//                }
            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

}
