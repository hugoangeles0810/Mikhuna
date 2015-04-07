package com.jasoftsolutions.mikhuna.util;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hugo on 26/03/2015.
 */
public class ValidationUtil {

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public static boolean isNotBlankText(String text) {
        if (text == null) {
            return false;
        } else {
            return !text.trim().equals("");
        }
    }

    public static boolean isNotBlankTexts(String... args) {
        for (String field : args) {
            if (!isNotBlankText(field)) return false;
        }
        return true;
    }

    public static boolean isPasswordsEquals(String password, String confirm) {
        if (password == null || confirm == null) return false;

        return password.equals(confirm);

    }

    public static boolean isTextLengthBetween(String text, int min, int max) {
        return text.length() >= min && text.length() <= max;
    }

    public static boolean isAlphanumericText(String text) {
        Pattern pattern = Pattern.compile("^[0-9a-zA-Z]+$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
}
