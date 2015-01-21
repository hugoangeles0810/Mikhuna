package com.jasoftsolutions.mikhuna.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantListFilterFragment;
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantListFragment;
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantPromotionsListFragment;
import com.jasoftsolutions.mikhuna.activity.listener.ApplyActionListener;
import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.domain.RestaurantListFilter;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

public class RestaurantListActivity extends BaseActivity
        implements ApplyActionListener, ActionBar.TabListener{

    private static final String TAG = RestaurantListActivity.class.getSimpleName();

    private Menu menu;

    private ViewPager viewPager;

    private RestaurantListFragment restaurantListFragment;
    private RestaurantListFilterFragment restaurantListFilterFragment;
    private RestaurantPromotionsListFragment restaurantPromotionsListFragment;

    private RestaurantListFilterPreferences restaurantListFilterPreferences;

    private boolean promotionListAudited;
    private boolean tabsReady;

    private void initializeFragments() {
        Log.i(TAG, "inicializando fragments...");
        tabsReady = false;
        promotionListAudited = false;

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        RestaurantListFilter filter = restaurantListFilterPreferences.loadFilter();

        // listado de restaurantes
        restaurantListFragment = new RestaurantListFragment();
        restaurantListFragment.setRestaurantListFilter(filter);

        // filtros de los listados
        restaurantListFilterFragment = new RestaurantListFilterFragment();
        restaurantListFilterFragment.setApplyActionListener(this);
        restaurantListFilterFragment.setCurrentFilter(filter);

        // listado de promociones
        restaurantPromotionsListFragment = new RestaurantPromotionsListFragment();
        restaurantPromotionsListFragment.setRestaurantListFilter(filter);

        viewPager = (ViewPager)findViewById(R.id.view_pager);

        RestaurantListSectionsPagerAdapter adapter = new RestaurantListSectionsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        setCurrentPage(RestaurantListSectionsPagerAdapter.RESTAURANT_LIST_POSITION);

        // inicializar tabs
        initializeSections(actionBar, adapter);
        tabsReady = true;
        actionBar.setSelectedNavigationItem(RestaurantListSectionsPagerAdapter.RESTAURANT_LIST_POSITION);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                showOptionsMenu(position);
                actionBar.setSelectedNavigationItem(position);
            }
        });
    }

    private void initializeSections(final ActionBar actionBar, PagerAdapter adapter) {
        for (int i = 0; i < adapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
    }

    private void refreshRestaurantListBasedOnFilters() {
        RestaurantListFilter newFilter = restaurantListFilterFragment.getCurrentFilter();
        RestaurantListFilter currentFilter = restaurantListFragment.getRestaurantListFilter();

        if (currentFilter == null || !newFilter.equals(currentFilter)) {
            setListsRestaurantListFilter(newFilter);
            refreshLists();
//            if (restaurantListFragment != null) {
//                restaurantListFragment.setRestaurantListFilter(newFilter);
//                restaurantListFragment.refreshRestaurantList();
//            }
//            if (restaurantPromotionsListFragment != null) {
//                restaurantPromotionsListFragment.setRestaurantListFilter(newFilter);
//                restaurantPromotionsListFragment.refreshRestaurantPromotionsList();
//            }
        }
    }

    private RestaurantListFragment getRestaurantListFragment() {
        return restaurantListFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        restaurantListFilterPreferences = new RestaurantListFilterPreferences(getApplicationContext());

        setContentView(R.layout.activity_restaurant_list);

        initializeFragments();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        Log.i(TAG, "creando el menu...");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.restaurant_list, menu);
        getMenuInflater().inflate(R.menu.common, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        setupSearchView(searchView);

        return true;
    }

    private void showOptionsMenu(int position) {
        try {
            boolean isListPosition = (RestaurantListSectionsPagerAdapter.RESTAURANT_LIST_POSITION == position);
            boolean isPromotionsPosition = (RestaurantListSectionsPagerAdapter.RESTAURANT_LIST_PROMOTIONS_POSITION == position);

            boolean isListOrPromotionsPosition = isListPosition || isPromotionsPosition;

            menu.findItem(R.id.action_refresh).setVisible(isListOrPromotionsPosition);
            menu.findItem(R.id.action_search).setVisible(isListPosition);
//            menu.findItem(R.id.action_filter).setVisible(isListOrPromotionsPosition);
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    private void setupSearchView(final SearchView searchView) {
        searchView.setQueryHint(getString(R.string.search_name_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                Log.i(TAG, "Realizando búsqueda... " + s);
//                restaurantListFragment.setRestaurantNameQuery(s);
//                restaurantListFragment.refreshRestaurantList();
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.i(TAG, "Realizando búsqueda... " + s);
                setListsRestaurantNameQuery(s);
                refreshLists();
//                searchView.clearFocus();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                String query = restaurantListFragment.getRestaurantNameQuery();
                if (query != null && query.trim().length() > 0) {
                    setListsRestaurantNameQuery(null);
                    refreshLists();
                }
                return false;
            }
        });
    }

    private void setListsRestaurantNameQuery(String s) {
        restaurantListFragment.setRestaurantNameQuery(s);
        restaurantPromotionsListFragment.setRestaurantNameQuery(s);
    }

    private void setListsRestaurantListFilter(RestaurantListFilter filter) {
        if (restaurantListFragment != null) {
            restaurantListFragment.setRestaurantListFilter(filter);
        }
        if (restaurantPromotionsListFragment != null) {
            restaurantPromotionsListFragment.setRestaurantListFilter(filter);
        }
    }

    private void refreshLists() {
        if (restaurantListFragment != null) {
            restaurantListFragment.refreshRestaurantList();
        }
        if (restaurantPromotionsListFragment != null) {
            restaurantPromotionsListFragment.refreshRestaurantPromotionsList();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (R.id.action_refresh == id) {
            if (viewPager != null) {
                if (viewPager.getCurrentItem() == RestaurantListSectionsPagerAdapter.RESTAURANT_LIST_POSITION) {
                    if (restaurantListFragment != null) {
                        restaurantListFragment.refreshRestaurantList();
                        return true;
                    }
                } else if (viewPager.getCurrentItem() == RestaurantListSectionsPagerAdapter.RESTAURANT_LIST_PROMOTIONS_POSITION) {
                    if (restaurantPromotionsListFragment != null) {
                        restaurantPromotionsListFragment.refreshRestaurantPromotionsList();
                        return true;
                    }
                }
            }
        }

        if (CommonMenuHandler.handleCommonMenus(this, item)) return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (RestaurantListSectionsPagerAdapter.RESTAURANT_LIST_POSITION != viewPager.getCurrentItem()) {
            setCurrentPage(RestaurantListSectionsPagerAdapter.RESTAURANT_LIST_POSITION);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onApplyAction(Object sender) {
        if (sender == restaurantListFilterFragment) {
            restaurantListFilterPreferences.saveFilter(restaurantListFilterFragment.getCurrentFilter());
            setCurrentPage(RestaurantListSectionsPagerAdapter.RESTAURANT_LIST_POSITION);
            refreshRestaurantListBasedOnFilters();
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        setCurrentPage(tab.getPosition());
    }

    private void setCurrentPage(int position) {
        try {
            viewPager.setCurrentItem(position);
            if (position == RestaurantListSectionsPagerAdapter.RESTAURANT_LIST_PROMOTIONS_POSITION
                    && tabsReady && !promotionListAudited) {
                Log.i(TAG, "invocando las promociones");
                promotionListAudited = true;
                new AuditHelper(this).registerPromotionListAction();
                AnalyticsUtil.registerEvent(this, AnalyticsConst.Category.LIST_RESTAURANT,
                        AnalyticsConst.Action.PROMOTION_LIST);
            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    private class RestaurantListSectionsPagerAdapter extends FragmentPagerAdapter {

        public static final int RESTAURANT_LIST_PROMOTIONS_POSITION = 0;
        public static final int RESTAURANT_LIST_POSITION = 1;
        public static final int RESTAURANT_LIST_FILTER_POSITION = 2;

        public RestaurantListSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (RESTAURANT_LIST_POSITION == position) {
                return restaurantListFragment;
            } else if (RESTAURANT_LIST_FILTER_POSITION == position) {
                return restaurantListFilterFragment;
            } else if (RESTAURANT_LIST_PROMOTIONS_POSITION == position) {
                return restaurantPromotionsListFragment;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (RESTAURANT_LIST_POSITION == position) {
                return getString(R.string.title_activity_tab_list);
            } else if (RESTAURANT_LIST_FILTER_POSITION == position) {
                return getString(R.string.title_activity_tab_filter);
            } else if (RESTAURANT_LIST_PROMOTIONS_POSITION == position) {
                return getString(R.string.title_activity_tab_promotions);
            } else {
                return "";
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
