package com.jasoftsolutions.mikhuna.activity.fragment.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.jasoftsolutions.mikhuna.R;

/**
 * Created by pc07 on 21/10/2014.
 */
public class Dialogs {

    public static AlertDialog noInternetConnectionMessage(Context context) {
        AlertDialog ad = new AlertDialog.Builder(context)
                .setMessage(R.string.message_dialog_no_server_response)
                .setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return ad;
    }

    public static AlertDialog dialogWithTitleAndMessage(Context context, String title, String msg){
        AlertDialog ad = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return ad;
    }

    public static AlertDialog dialogWithMessage(Context context, String msg){
        AlertDialog ad = new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return ad;
    }

}
