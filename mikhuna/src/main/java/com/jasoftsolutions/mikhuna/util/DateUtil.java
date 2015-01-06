package com.jasoftsolutions.mikhuna.util;

import android.content.Context;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.domain.Weekday;
import com.jasoftsolutions.mikhuna.remote.Const;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pc07 on 03/04/2014.
 */
public class DateUtil {

    public static final String AM_STRING = "am.";
    public static final String PM_STRING = "pm.";

    public static SimpleDateFormat getUserSimpleDateFormat() {
        return new SimpleDateFormat(Const.DEFAULT_USER_DATE_FORMAT);
    }

    public static SimpleDateFormat getUserMediumDateFormat(Context context) {
        return new SimpleDateFormat(context.getString(R.string.short_date));
    }

    public static Weekday getCurrentWeekday() {
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.MONDAY) {
            return Weekday.MONDAY;
        } else if (day == Calendar.TUESDAY) {
            return Weekday.TUESDAY;
        } else if (day == Calendar.WEDNESDAY) {
            return Weekday.WEDNESDAY;
        } else if (day == Calendar.THURSDAY) {
            return Weekday.THURSDAY;
        } else if (day == Calendar.FRIDAY) {
            return Weekday.FRIDAY;
        } else if (day == Calendar.SATURDAY) {
            return Weekday.SATURDAY;
        } else if (day == Calendar.SUNDAY) {
            return Weekday.SUNDAY;
        }
        return null;
    }

    public static long getCurrentTime() {
        return new Date().getTime() / 1000; //  Obtener sólo los segundos
    }

    public static String from24HToAmPm(String timestr) {
        if (timestr.equals("00:00")) {
            return "12:00 " + AM_STRING;
        } else if (timestr.equals("12:00")) {
            return "12:00 " + PM_STRING;
        } else {
            int compare = "12:00".compareTo(timestr);
            if (compare < 0) {
                String[] parts = timestr.split(":");
                int hour = Integer.parseInt(parts[0]);
                hour -= 12;
                return String.format("%02d", hour) + ":" + parts[1] + " " + PM_STRING;
            } else {
                return timestr + " am.";
            }
        }
    }
}
