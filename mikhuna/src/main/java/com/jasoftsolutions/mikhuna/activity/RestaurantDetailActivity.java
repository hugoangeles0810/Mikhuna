package com.jasoftsolutions.mikhuna.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantDetailFragment;
import com.jasoftsolutions.mikhuna.activity.fragment.dialog.Dialogs;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.store.RestaurantStore;
import com.jasoftsolutions.mikhuna.store.StoreAdapter;
import com.jasoftsolutions.mikhuna.store.StoreListener;
import com.jasoftsolutions.mikhuna.util.ContextUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.UiUtil;

public class RestaurantDetailActivity extends BaseActivity implements
        StoreListener {

    private static final String TAG = RestaurantDetailActivity.class.getSimpleName();

    private Restaurant restaurant;

    private long restaurantId;
    private long restaurantServerId;
    private String restaurantUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        restaurantId = ContextUtil.getIntentLongExtra(getIntent(), ArgKeys.RESTAURANT_ID);
        if (restaurantId == 0) {
            restaurantServerId =  ContextUtil.getIntentLongExtra(getIntent(), ArgKeys.RESTAURANT_SERVER_ID);
            if (restaurantServerId == 0) {
                restaurantUri = getIntent().getStringExtra(ArgKeys.RESTAURANT_URI);
            }
        }

        if (savedInstanceState == null) {
            loadRestaurant();
        } else {
            refreshRestaurant();
        }
    }

    private void loadRestaurant() {
        UiUtil.removeAllFragmentsAndAddLoadingFragment(this);
        final RestaurantStore rs = RestaurantStore.getInstance();
        rs.addListener(this);

        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                if ((restaurantId == 0 || restaurantServerId == 0) && restaurantUri != null) {
                    rs.requestRestaurantServerIdFromUri(new StoreAdapter() {
                        @Override
                        public void onSuccess(Object sender, Object data) {
                            restaurantServerId = (Long)data;
                            requestRestaurantById();
                        }
                    }, restaurantUri);
                } else {
                    requestRestaurantById();
                }
            }
        });
        task.run();
    }

    private void requestRestaurantById() {
        RestaurantStore rs = RestaurantStore.getInstance();
        if (restaurantId > 0) {
            rs.requestRestaurantByLocalId(RestaurantDetailActivity.this, restaurantId);
        } else {
            if (restaurantServerId > 0) {
                rs.requestRestaurant(RestaurantDetailActivity.this, restaurantServerId);
            }
        }
    }

    private void refreshRestaurant() {
        RestaurantStore rs = RestaurantStore.getInstance();
        if (restaurantId > 0) {
            rs.requestRestaurantByLocalId(this, restaurantId, true);
        } else {
            rs.requestRestaurant(this, restaurantServerId, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RestaurantStore rs = RestaurantStore.getInstance();
        rs.addListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RestaurantStore rs = RestaurantStore.getInstance();
        rs.removeListener(this);
    }

    private void showRestaurant(Restaurant r, boolean withRetry) {
        try {
            restaurant = r;
            restaurantId = r.getId();
            restaurantServerId = r.getServerId();

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            UiUtil.removeLoadingFragmentIfExistsOnTransaction(fm, ft);

            RestaurantDetailFragment rdf = (RestaurantDetailFragment) fm.findFragmentByTag(Tags.FRAGMENT_DETAIL);
            if (rdf == null) {
                rdf = new RestaurantDetailFragment();
                ft.add(R.id.container, rdf, Tags.FRAGMENT_DETAIL);
            }
            if (withRetry) {
                showRetryDialog();
            }

            ft.commit();

            rdf.display(r);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    private void showRetryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.restaurant_detail_incomplete_data);
        builder.setPositiveButton(R.string.dialog_button_retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadRestaurant();
            }
        });
        builder.setNeutralButton(R.string.dialog_button_continue, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        try {
            builder.create().show();
        } catch (WindowManager.BadTokenException bte) {
            ExceptionUtil.ignoreException(bte);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

//    private void showRetry() {
//        try {
//            FragmentManager fm = getSupportFragmentManager();
//            FragmentTransaction ft = fm.beginTransaction();
//
//            UiUtil.removeLoadingFragmentIfExistsOnTransaction(fm, ft);
//
//            RestaurantDetailFragment rdf = (RestaurantDetailFragment) fm.findFragmentByTag(Tags.FRAGMENT_DETAIL);
//            if (rdf == null) {
//                rdf = new RestaurantDetailFragment();
//                ft.add(R.id.container, rdf, Tags.FRAGMENT_DETAIL);
//                rdf.display(restaurant);
//            }
//
//            ft.commit();
//        } catch (Exception e) {
//            ExceptionUtil.handleException(e);
//        }
//    }

    @Override
    public void onTimeOut(Object sender, @Nullable final Object data) {}

    @Override
    public void onUpdate(Object sender, final Object data) {}

    @Override
    public void onReady(Object sender, final Object data) {}

    @Override
    public void onFailedConnection(Object sender, @Nullable final Object data) {}

    @Override
    public void onSuccess(Object sender, final Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showRestaurant((Restaurant) data, false);
            }
        });
    }

    @Override
    public void onFail(Object sender, final Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data != null) {
                    showRestaurant((Restaurant) data, true);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.restaurant_detail, menu);
        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//        if (id == R.id.action_rate_restaurant) {
//            showRestaurantRating();
//            return true;
//        } else if (id == R.id.action_share) {
        if (id == R.id.action_share) {
            shareRestaurant();
            return true;
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(ArgKeys.RESTAURANT, restaurant);
        if (CommonMenuHandler.handleCommonMenus(this, item, bundle)) return true;

        return super.onOptionsItemSelected(item);
    }

    private void shareRestaurant() {
//        String message = getText(R.string.restaurant_sharing_default_message) + "\n";
//        message += Const.BACKEND_BASE_URL + "/Link/" + restaurant.getServerId();
        try {
            RestaurantStore rs = RestaurantStore.getInstance();
            rs.requestRestaurantFullUrlFromServerId(new StoreAdapter() {
                @Override
                public void onSuccess(Object sender, Object data) {
                    String url = Const.BACKEND_BASE_URL + "/" + data.toString();
                    String message = getString(R.string.restaurant_sharing_default_message,
                            restaurant.getName(), url);
                    ContextUtil.sendTextIntent(RestaurantDetailActivity.this, message,
                            R.string.dialog_share_restaurant);

                    new AuditHelper(RestaurantDetailActivity.this).registerShareRestaurantActionOf(restaurant);
                }

                @Override
                public void onFail(Object sender, Object data) {
                    RestaurantDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Dialogs.noInternetConnectionMessage(RestaurantDetailActivity.this)
                                    .show();
                        }
                    });
                }
            }, restaurantServerId);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    public void onRetryButtonClick(View v) {
        Toast.makeText(this, "retry...", Toast.LENGTH_LONG).show();
    }

    public static Intent getLauncherIntent(Context context, Long restaurantId) {
        Intent intent = new Intent(context, RestaurantDetailActivity.class);
        intent.putExtra(ArgKeys.RESTAURANT_ID, restaurantId);
        return intent;
    }

    public static Intent getLauncherIntentByServerId(Context context, Long restaurantServerId) {
        Intent intent = new Intent(context, RestaurantDetailActivity.class);
        intent.putExtra(ArgKeys.RESTAURANT_SERVER_ID, restaurantServerId);
        return intent;
    }

    public static Intent getLauncherIntentByUri(Context context, String uri) {
        Intent intent = new Intent(context, RestaurantDetailActivity.class);
        intent.putExtra(ArgKeys.RESTAURANT_URI, uri);
        return intent;
    }

}
