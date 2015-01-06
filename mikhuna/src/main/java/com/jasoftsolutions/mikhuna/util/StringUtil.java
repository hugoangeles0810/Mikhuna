package com.jasoftsolutions.mikhuna.util;

/**
 * Created by pc07 on 04/04/2014.
 */
public class StringUtil {

    public static String currencyFormat(String currency, Number amount) {
        return currency + " " + amount.toString();
    }


}
