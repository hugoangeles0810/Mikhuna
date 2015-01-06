package com.jasoftsolutions.mikhuna.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.util.AccountUtil;
import com.jasoftsolutions.mikhuna.util.ContextUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

/**
 * Created by pc07 on 06/05/2014.
 */
public class CommonMenuHandler {

    public static boolean handleCommonMenus(Context context, MenuItem menuItem, Bundle extras) {
        int id = menuItem.getItemId();

        if (R.id.action_report_problem == id) {
            Intent intent = new Intent(context, ProblemReportActivity.class);
            if (extras != null) {
                intent.putExtras(extras);
            }
            context.startActivity(intent);
            return true;
        } else if (R.id.action_contact == id) {
            showAbout(context);
            return true;
        }

        return false;
    }

    public static boolean handleCommonMenus(Context context, MenuItem menuItem) {
        return handleCommonMenus(context, menuItem, null);
    }

    private static void showAbout(Context context) {
        try {
            String url = Const.BACKEND_BASE_URL + "/Contact/mobile/email/"
                    + AccountUtil.getDefaultGoogleAccount(context);
            ContextUtil.navigateWebUrl(context, url);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

}
