package com.jasoftsolutions.mikhuna.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.fragment.dialog.Dialogs;
import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.Version;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.remote.LastResourceUpdatePreferences;
import com.jasoftsolutions.mikhuna.remote.ManagementUpdaterThread;
import com.jasoftsolutions.mikhuna.routing.RoutingHandler;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;
import com.jasoftsolutions.mikhuna.util.ContextUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.ThreadUtil;
import com.jasoftsolutions.mikhuna.util.TriggerQueue;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ManagementUpdaterThread managementUpdaterThread;

    private Long restaurantId;
    private Long restaurantServerId;
    private Restaurant restaurant;

    private String message;

    private TriggerQueue triggerQueue;

    private Integer intentSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadManagementData();

        triggerQueue = new TriggerQueue(3, new Runnable() {
            @Override
            public void run() {
                showNextActivity();
            }
        });

        if (!RoutingHandler.handleRouter(this, getIntent())) {
            checkExtras(savedInstanceState);
        } else {
            finish();
        }
    }

    private void waitForSplash() {
        ThreadUtil.timeout(new Runnable() {
            @Override
            public void run() {
                triggerQueue.completeOne();
            }
        }, Const.SPLASH_MIN_DELAY);
    }

    private void checkExtras(Bundle savedInstance) {
        Bundle data = getIntent().getExtras();
        if (data == null) {
            data = savedInstance;
        }
        if (data != null) {
            if (data.containsKey(ArgKeys.SOURCE)) {
                intentSource = data.getInt(ArgKeys.SOURCE);
            }
            if (data.containsKey(ArgKeys.RESTAURANT_ID)) {
                restaurantServerId = data.getLong(ArgKeys.RESTAURANT_ID);
                restaurant = new RestaurantManager().getRestaurantByServerId(restaurantServerId);
                if (restaurant != null) {
                    restaurantId = restaurant.getId();
                    if (intentSource == ArgKeys.SOURCE_GCM) {
                        new AuditHelper(this).registerViewGCM(restaurant);
                        AnalyticsUtil.registerEvent(this, AnalyticsConst.Category.ENGAGEMENT,
                                AnalyticsConst.Action.VIEW_GCM, restaurant.getServerId().toString());
                    }
                }
            }
            if (data.containsKey(ArgKeys.MESSAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(data.getString(ArgKeys.MESSAGE))
                        .setCancelable(false)
                        .setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                triggerQueue.completeOne();
                            }
                        });
                builder.create().show();
            } else {
                triggerQueue.completeOne();
            }
            // Ya no debe esperar en la pantalla de splash
            triggerQueue.completeOne();
        } else {
            triggerQueue.completeOne();
            // Esperar la pantalla de splash
            waitForSplash();
        }
    }

    private void loadManagementData() {
        new InitialDataLoadTask().execute();
    }

    private void showNextActivity() {
        Intent intent = null;

        ContextUtil.asyncSendGcmToBackend(this);

        if (restaurantId != null) {
            intent = RestaurantDetailActivity.getLauncherIntent(this, restaurantId);

            if (intentSource == ArgKeys.SOURCE_GCM && restaurant != null) {
                new AuditHelper(this).registerGCMViewDetailOf(restaurant);
                AnalyticsUtil.registerEvent(this, AnalyticsConst.Category.ENGAGEMENT,
                        AnalyticsConst.Action.VIEW_DETAIL_FROM_GCM, restaurant.getServerId().toString());
            }
        } else {
            if (new RestaurantManager().countAllRestaurants() == 0
                    || new RestaurantListFilterPreferences(this).getUbigeoId() == 0) {
                intent = new Intent(this, FirstTimeActivity.class);
            } else {
                intent = new Intent(this, RestaurantListActivity.class);
            }
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.restaurant_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private class InitialDataLoadTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            managementUpdaterThread = ManagementUpdaterThread.getInstance(MainActivity.this);
            managementUpdaterThread.startIfNecessary();

            try {
                managementUpdaterThread.join(Const.MAX_UPDATING_MILLISECONDS_DELAY);
            } catch (InterruptedException ie) {
                ExceptionUtil.handleException(ie);
            }

            if (managementUpdaterThread.getResult() != null && managementUpdaterThread.getResult()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            LastResourceUpdatePreferences lru = new LastResourceUpdatePreferences(MainActivity.this);
            final Version version = lru.getVersion();
            if (result) {
                if (version.getState() == Version.VERSION_OK) {
                    triggerQueue.completeOne();
                }else{
                    showDialog(Dialogs.dialogUpdateApp(MainActivity.this, version));
                }
            } else {

                AlertDialog dialog;
                if (version.getState() == Version.VERSION_OK){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);

                    builder.setMessage(R.string.message_dialog_no_server_response);

                    builder.setPositiveButton(R.string.dialog_button_retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loadManagementData();
                        }
                    });

                    if (lru.getManagementLastUpdate() > 0) {
                        builder.setNeutralButton(R.string.dialog_button_continue, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                triggerQueue.completeOne();
                            }
                        });
                    } else {
                        builder.setNegativeButton(R.string.dialog_button_close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                    }

                    dialog = builder.create();
                }else{
                    dialog = Dialogs.dialogUpdateApp(MainActivity.this, version);
                }

                showDialog(dialog);
            }
        }
    }

    public void showDialog(AlertDialog dialog){
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException bte) {
            ExceptionUtil.ignoreException(bte);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }
}
