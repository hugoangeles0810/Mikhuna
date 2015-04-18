package com.jasoftsolutions.mikhuna.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantDishCategoryFragment;
import com.jasoftsolutions.mikhuna.activity.fragment.dialog.Dialogs;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.RestaurantDishCategory;
import com.jasoftsolutions.mikhuna.service.ServiceSendLikeDish;
import com.jasoftsolutions.mikhuna.store.RestaurantStore;
import com.jasoftsolutions.mikhuna.store.StoreListener;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo on 10/01/2015.
 */
public class RestaurantDishCategoryActivity extends BaseActivity implements
        StoreListener{

    private static final String TAG = RestaurantDishCategoryActivity.class.getSimpleName();

    private String restaurantName;
    private Long restaurantServerId;

    private RestaurantDishCategoryFragment dishCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_carte);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);

        if (savedInstanceState == null){
            restaurantServerId = getIntent().getExtras().getLong(ArgKeys.RESTAURANT_SERVER_ID);
            restaurantName = getIntent().getExtras().getString(ArgKeys.RESTAURANT_NAME);
        }else{
            restaurantServerId = savedInstanceState.getLong(ArgKeys.RESTAURANT_SERVER_ID);
            restaurantName = savedInstanceState.getString(ArgKeys.RESTAURANT_NAME);
        }

        this.setTitle(getResources().getString(R.string.title_activity_restaurant_carte) + " - " + restaurantName);
        loadRestaurantDishCategory();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

    private void loadRestaurantDishCategory() {
        UiUtil.removeAllFragmentsAndAddLoadingFragment(this);
        RestaurantStore rs = RestaurantStore.getInstance();
        rs.addListener(this);
        rs.requestRestaurantDishCategoriesOf(restaurantServerId, this, this);
    }

    private void showDishCategories(ArrayList<RestaurantDishCategory> dc, Boolean withRetry) {
        try {
            ArrayList<RestaurantDishCategory> dishCategories = dc;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            UiUtil.removeLoadingFragmentIfExistsOnTransaction(fm, ft);

            RestaurantDishCategoryFragment rdcf = (RestaurantDishCategoryFragment) fm.findFragmentByTag(Tags.FRAGMENT_DATA);
            if (rdcf == null){
                rdcf = new RestaurantDishCategoryFragment();
                ft.add(R.id.container, rdcf, Tags.FRAGMENT_DATA);
            }

            if (withRetry){
                showRetryDialog();
            }

            ft.commit();
            rdcf.display(dishCategories);
        }catch (Exception e){
            ExceptionUtil.ignoreException(e);
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
        Intent i = ServiceSendLikeDish.getIntentOfActionSendLike(this);
        startService(i);
        RestaurantStore rs = RestaurantStore.getInstance();
        rs.removeListener(this);
        super.onStop();
    }

    @Override
    public void onReady(Object sender, Object data) {

    }

    @Override
    public void onUpdate(Object sender, Object data) {

    }

    @Override
    public void onTimeOut(Object sender, @Nullable Object data) {

    }

    @Override
    public void onFailedConnection(Object sender, @Nullable Object data) {

    }

    @Override
    public void onSuccess(Object sender, final Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            if (!(data instanceof Long))
            showDishCategories((ArrayList<RestaurantDishCategory>) data, false);
            }
        });
    }

    @Override
    public void onFail(Object sender, final Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            showDishCategories((ArrayList<RestaurantDishCategory>) data, true);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(ArgKeys.RESTAURANT_SERVER_ID, restaurantServerId);
        outState.putString(ArgKeys.RESTAURANT_NAME, restaurantName);
        super.onSaveInstanceState(outState);
    }

    private void showRetryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.restaurant_carte_incomplete);
        builder.setPositiveButton(R.string.dialog_button_retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadRestaurantDishCategory();
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

    public static Intent getLauncherIntent(Context context, Restaurant restaurant) {
        Intent intent = new Intent(context, RestaurantDishCategoryActivity.class);
        intent.putExtra(ArgKeys.RESTAURANT_SERVER_ID, restaurant.getServerId());
        intent.putExtra(ArgKeys.RESTAURANT_NAME, restaurant.getName());
        return intent;
    }
}
