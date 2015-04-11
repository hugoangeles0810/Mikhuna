package com.jasoftsolutions.mikhuna.activity.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.RestaurantDetailActivity;
import com.jasoftsolutions.mikhuna.activity.adapter.RestaurantPromotionListReducedAdapter;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.domain.RestaurantListFilter;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.model.RestaurantPromotion;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.remote.RestaurantPromotionsUpdaterTask;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

import java.util.ArrayList;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantPromotionsListFragment extends Fragment {

    private static final String TAG = RestaurantPromotionsListFragment.class.getSimpleName();

    private FrameLayout loadingLayout;
    private FrameLayout listLayout;
    private ListView promotionsListView;
    private FrameLayout emptyRestaurantPromotionsListFrame;
    private TextView emptyRestaurantPromotionsListMessage;

    private RestaurantListFilter restaurantListFilter;
    private String restaurantNameQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_promotions, container, false);

//        List<Restaurant> restaurantList=null;

//        if (getArguments()!=null) {
//            restaurantList=(List<Restaurant>)getArguments().getSerializable(ARGKEY_RESTAURANT_LIST);
//        } else if (savedInstanceState!=null) {
//            restaurantList=(List<Restaurant>)savedInstanceState.getSerializable(ARGKEY_RESTAURANT_LIST);
//        }

//        showRestaurantPromotionList(restaurantList, rootView);

        promotionsListView =(ListView)rootView.findViewById(R.id.restaurant_all_promotions_list);
        promotionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Restaurant restaurant = ((RestaurantPromotion)adapterView.getItemAtPosition(i)).getRestaurant();

                Intent detailIntent = new Intent(view.getContext(), RestaurantDetailActivity.class);
                detailIntent.putExtra("restaurant_id", restaurant.getId());

                startActivity(detailIntent);

                new AuditHelper(view.getContext()).registerPromotionListViewDetailActionOf(restaurant);
                AnalyticsUtil.registerEvent(view.getContext(), AnalyticsConst.Category.LIST_RESTAURANT,
                        AnalyticsConst.Action.VIEW_DETAIL_FROM_PROMOTION_LIST, restaurant.getServerId().toString());
            }
        });

        emptyRestaurantPromotionsListFrame = (FrameLayout)rootView.findViewById(R.id.empty_restaurant_all_promotions_list_frame);
        emptyRestaurantPromotionsListMessage = (TextView)rootView.findViewById(R.id.empty_restaurant_all_promotions_list_message);

        loadingLayout = (FrameLayout)rootView.findViewById(R.id.frame_loading);
        listLayout = (FrameLayout)rootView.findViewById(R.id.restaurant_all_promotions_list_frame);

//        new AuditHelper(getActivity()).registerPromotionListAction();

        refreshRestaurantPromotionsList();

        return rootView;
    }

    private void showRestaurantPromotionList(ArrayList<RestaurantPromotion> restaurantPromotionList) {
        Log.i(TAG, String.valueOf(this.hashCode()));
        System.out.println(restaurantPromotionList);
        System.out.println(getView());

        if (restaurantPromotionList == null) return;

        if (promotionsListView != null && getActivity() != null) {
            RestaurantPromotionListReducedAdapter rla=new RestaurantPromotionListReducedAdapter(getActivity(), restaurantPromotionList);
            promotionsListView.setAdapter(rla);
        }
    }

    public RestaurantListFilter getRestaurantListFilter() {
        return restaurantListFilter;
    }
    public void setRestaurantListFilter(RestaurantListFilter restaurantListFilter) {
        this.restaurantListFilter = restaurantListFilter;
//        refreshRestaurantList();
    }

    public String getRestaurantNameQuery() {
        return restaurantNameQuery;
    }
    public void setRestaurantNameQuery(String restaurantNameQuery) {
        this.restaurantNameQuery = restaurantNameQuery;
//        refreshRestaurantList();
    }

    private void showLoading(boolean show) {
        try {
            if (show) {
                loadingLayout.setVisibility(View.VISIBLE);
                listLayout.setVisibility(View.GONE);
            } else {
                loadingLayout.setVisibility(View.GONE);
                listLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    private void showEmptyListMessage(boolean show) {
        if (show) {
            promotionsListView.setVisibility(View.GONE);
            emptyRestaurantPromotionsListFrame.setVisibility(View.VISIBLE);
        } else {
            promotionsListView.setVisibility(View.VISIBLE);
            emptyRestaurantPromotionsListFrame.setVisibility(View.GONE);
        }
    }

    public void refreshRestaurantPromotionsList() {
        new AsyncTask<Void, Void, ArrayList<RestaurantPromotion>>() {

            @Override
            protected void onPreExecute() {
                showLoading(true);
            }

            @Override
            protected ArrayList<RestaurantPromotion> doInBackground(Void... params) {
                RestaurantPromotionsUpdaterTask task = RestaurantPromotionsUpdaterTask.getInstance(getActivity());
                task.startIfNecessary();

                try {
                    task.join(Const.MAX_UPDATING_MILLISECONDS_DELAY);
                } catch (InterruptedException ie) {
                    ExceptionUtil.handleException(ie);
                }

                return new RestaurantManager().queryValidPromotionsAndLoadRestaurants(getRestaurantNameQuery(),
                        getRestaurantListFilter());
            }

            @Override
            protected void onPostExecute(ArrayList<RestaurantPromotion> restaurantPromotionList) {
                showLoading(false);
                if (restaurantPromotionList != null && restaurantPromotionList.size() > 0) {
                    showEmptyListMessage(false);
                    showRestaurantPromotionList(restaurantPromotionList);
                } else {
                    showEmptyListMessage(true);
                }
            }
        }.execute();
    }

}
