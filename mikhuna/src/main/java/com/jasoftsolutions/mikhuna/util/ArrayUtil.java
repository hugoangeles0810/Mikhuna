package com.jasoftsolutions.mikhuna.util;

import java.util.ArrayList;

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
}
