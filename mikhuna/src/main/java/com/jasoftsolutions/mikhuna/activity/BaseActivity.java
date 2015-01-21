package com.jasoftsolutions.mikhuna.activity;

import android.support.v7.app.ActionBarActivity;

import com.google.analytics.tracking.android.EasyTracker;
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

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }
}
