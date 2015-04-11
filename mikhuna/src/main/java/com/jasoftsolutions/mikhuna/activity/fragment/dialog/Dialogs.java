package com.jasoftsolutions.mikhuna.activity.fragment.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.BaseActivity;
import com.jasoftsolutions.mikhuna.model.Version;

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

    public static AlertDialog dialogUpdateApp(final BaseActivity activity, final Version version){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);

        builder.setMessage(version.getMsg());
        builder.setPositiveButton(activity.getText(R.string.dialog_button_update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(version.getUrl()));
                activity.startActivity(i);
                activity.finish();
            }
        });

        builder.setNegativeButton(activity.getText(R.string.dialog_button_close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });

        return builder.create();
    }

}
