package com.jasoftsolutions.mikhuna.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantCarteCategoriesFragment;
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantCarteFragment;

public class RestaurantCarteActivity extends BaseActivity
    implements RestaurantCarteCategoriesFragment.Listener {

    private static final String TAG = RestaurantCarteActivity.class.getSimpleName();

    private RestaurantCarteFragment restaurantCarteFragment;

    private Long restaurantServerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_carte);

        restaurantServerId = getIntent().getLongExtra(ArgKeys.RESTAURANT_SERVER_ID, 0);

        restaurantCarteFragment = (RestaurantCarteFragment) getSupportFragmentManager()
                .findFragmentById(R.id.carte_fragment);

        if (restaurantCarteFragment != null) {
            restaurantCarteFragment.setRestaurantServerId(restaurantServerId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
//        getMenuInflater().inflate(R.menu.restaurant_detail, menu);
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

            return true;
        }

        Bundle bundle = new Bundle();
//        bundle.putSerializable(ArgKeys.RESTAURANT, restaurant);
        if (CommonMenuHandler.handleCommonMenus(this, item, bundle)) return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (restaurantCarteFragment.isCategoryViewVisible()) {
            super.onBackPressed();
        } else {
            restaurantCarteFragment.setCartePageCategory();
        }
    }

    @Override
    public void onSelectDishCategory(Long dishCategoryServerId) {
        restaurantCarteFragment.onSelectDishCategory(dishCategoryServerId);
    }




    public static Intent getLauncherIntent(Context context, long restaurantServerId) {
        Intent result = new Intent(context, RestaurantCarteActivity.class);
        result.putExtra(ArgKeys.RESTAURANT_SERVER_ID, restaurantServerId);

        return result;
    }

    public static void launch(Context context, long restaurantServerId) {
        context.startActivity(getLauncherIntent(context, restaurantServerId));
    }
}
