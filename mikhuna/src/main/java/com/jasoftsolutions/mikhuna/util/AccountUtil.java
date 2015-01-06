package com.jasoftsolutions.mikhuna.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * Created by pc07 on 07/04/2014.
 */
public class AccountUtil {

    public static String getDefaultGoogleAccount(Context context) {
        Account[] accounts= AccountManager.get(context).getAccountsByType("com.google");
        if (accounts.length>0) {
            return accounts[0].name;
        }
        return null;
    }

}
