package com.jasoftsolutions.mikhuna.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jasoftsolutions.mikhuna.R;

/**
 * Created by pc07 on 27/03/14.
 */
public class PhoneCall {

    public static void makeDialCall(Context context, String phoneNumber) {
        try {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:"+Uri.encode(phoneNumber.trim())));
            dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(dialIntent);
        } catch (Exception e) {
            ContextUtil.showWarningMessage(context, R.string.warning_dial_action_error);
            ExceptionUtil.handleException(e);
        }
    }

}
