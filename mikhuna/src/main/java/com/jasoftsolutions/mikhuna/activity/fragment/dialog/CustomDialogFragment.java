package com.jasoftsolutions.mikhuna.activity.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by pc07 on 23/04/2014.
 */
public class CustomDialogFragment extends DialogFragment {

    private AlertDialog.Builder builder;

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(AlertDialog.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return builder.create();
    }
}
