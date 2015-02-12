package com.jasoftsolutions.mikhuna.util;

import android.util.Log;

import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.RestaurantCategoryAssign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gin on 01/05/14.
 */
public class ArrayUtil {

    public static String implode(Iterable it, String glue) {
        StringBuilder result = new StringBuilder();
        for (Object o : it) {
            result.append(glue).append(o);
        }
        if (result.length() > 0) {
            result.delete(0, glue.length());
        }
        return result.toString();
    }

    public static ArrayList<Long> getArrayListOfLongFromString(
            String src, String separator) {
        ArrayList<Long> result = new ArrayList<Long>();

        String[] strNumbers = src.split(separator);

        for (String str : strNumbers) {
            result.add(new Long(str));
        }

        return result;
    }

    public static ArrayList<Restaurant> filterFromCategoriesAndServices(
            List<Restaurant> data, List<Long> categories, List<Long> services){
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        for (Restaurant r : data){
            boolean contain;

            contain = r.getServiceTypeId()==3 ||
                    services.contains(Long.parseLong(r.getServiceTypeId() + ""));

            if (contain) {
                contain = false;
                for (RestaurantCategoryAssign categoryAssign : r.getRestaurantCategoryAssigns()) {
                    if (categories.contains(categoryAssign.getRestaurantCategoryServerId())) {
                        contain = true;
                        break;
                    }
                }
            }

            if (contain){
                restaurants.add(r);
            }
        }

        return restaurants;
    }
}
