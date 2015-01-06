package com.jasoftsolutions.mikhuna.activity;

import android.support.v7.app.ActionBarActivity;

import com.jasoftsolutions.mikhuna.util.SocialHelper;

/**
 * Created by mgaray on 02/10/14.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onResume() {
        super.onResume();
        SocialHelper.activateAppFacebookEventLog(this);
    }
}
