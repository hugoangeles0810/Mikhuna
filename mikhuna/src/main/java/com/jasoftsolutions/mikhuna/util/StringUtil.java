package com.jasoftsolutions.mikhuna.util;

import android.content.Context;

import com.jasoftsolutions.mikhuna.model.Service;

import java.util.List;

/**
 * Created by pc07 on 04/04/2014.
 */
public class StringUtil {

    public static String currencyFormat(String currency, Number amount) {
        return currency + " " + amount.toString();
    }

    public static String getServicesOfRestaurant(List<Service> services){
        StringBuilder sb = new StringBuilder();
        int len = services.size();
        Service s = null;
        for (int i=0; i<len-1; i++){
            s  = services.get(i);
            sb.append(s.getName() + ", ");
        }
        s = services.get(len-1);
        sb.append(s.getName()+".");

        return sb.toString();
    }

    public static String getStringForResourceId(Context context, int id){
        return context.getResources().getString(id);
    }


}
