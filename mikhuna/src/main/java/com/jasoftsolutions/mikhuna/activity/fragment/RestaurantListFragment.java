package com.jasoftsolutions.mikhuna.activity.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.RestaurantDetailActivity;
import com.jasoftsolutions.mikhuna.activity.adapter.RestaurantListAdapter;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.domain.RestaurantListFilter;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.remote.RestaurantsUpdaterTask;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.List;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantListFragment extends Fragment {

    private static final String TAG = RestaurantListFragment.class.getSimpleName();

    public static final String ARGKEY_RESTAURANT_LIST = "restaurant_list";

    private FrameLayout loadingLayout;
    private FrameLayout listViewContainerFrame;
    private ListView restaurantListView;
    private View restaurantListViewPanel;
    private FrameLayout emptyRestaurantListFrame;
    private TextView emptyRestaurantListMessage;

    private TextView restaurantCounter;
    private View restaurantCounterContainer;
    private boolean restaurantCounterAnimation;

    private RestaurantListFilter restaurantListFilter;
    private String restaurantNameQuery;


    /**
     * Indica cuál es la posición (en el adaptador de datos del listado) del último restaurant
     * al cual se ha visto su detalle
     */
    private Integer lastViewedRestaurantPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

//        List<Restaurant> restaurantList=null;

//        if (getArguments()!=null) {
//            restaurantList=(List<Restaurant>)getArguments().getSerializable(ARGKEY_RESTAURANT_LIST);
//        } else if (savedInstanceState!=null) {
//            restaurantList=(List<Restaurant>)savedInstanceState.getSerializable(ARGKEY_RESTAURANT_LIST);
//        }

//        showRestaurantList(restaurantList, rootView);
        restaurantListViewPanel = rootView.findViewById(R.id.restaurant_list_panel);
        restaurantCounter = (TextView) rootView.findViewById(R.id.restaurant_count);
        restaurantCounterContainer = rootView.findViewById(R.id.restaurant_count_container);
        restaurantListView=(ListView)rootView.findViewById(R.id.restaurant_list);
        restaurantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Restaurant restaurant=(Restaurant)adapterView.getItemAtPosition(i);

                Intent detailIntent = RestaurantDetailActivity.getLauncherIntent(view.getContext(),
                        restaurant.getId());
                lastViewedRestaurantPosition = i;

                startActivity(detailIntent);

                new AuditHelper(view.getContext()).registerViewDetailOf(restaurant);
                AnalyticsUtil.registerEvent(view.getContext(), AnalyticsConst.Category.DETAIL_RESTAURANT,
                        AnalyticsConst.Action.VIEW_DETAIL_FROM_RESTAURANT_LIST, restaurant.getServerId().toString());
            }
        });
        PauseOnScrollListener pauseListener = new PauseOnScrollListener(ImageLoader.getInstance(), false, false, new AbsListView.OnScrollListener() {
            private int lastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (lastVisibleItem < firstVisibleItem) {
                    hideRestaurantCounter();
                } else if (lastVisibleItem > firstVisibleItem) {
                    showRestaurantCounter();
                }
                lastVisibleItem = firstVisibleItem;
            }
        });
        restaurantListView.setOnScrollListener(pauseListener);

        emptyRestaurantListFrame = (FrameLayout)rootView.findViewById(R.id.empty_restaurant_list_frame);
        emptyRestaurantListMessage = (TextView)rootView.findViewById(R.id.empty_restaurant_list_message);

        loadingLayout = (FrameLayout)rootView.findViewById(R.id.frame_loading);

        listViewContainerFrame =(FrameLayout)rootView.findViewById(R.id.restaurant_list_container_frame);
//        ActionBarPullToRefresh.from(this.getActivity())
//                .allChildrenArePullable()
//                .setup(listViewContainerFrame);

        new AuditHelper(getActivity()).registerRestaurantListAction();
        AnalyticsUtil.registerEvent(getActivity(), AnalyticsConst.Category.LIST_RESTAURANT,
                AnalyticsConst.Action.RESTAURANT_LIST);

        refreshRestaurantList();
        return rootView;
    }

    private void showRestaurantCounter() {
        if (restaurantCounterContainer != null && restaurantCounterContainer.getVisibility() == View.GONE
                && !restaurantCounterAnimation) {
            Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    restaurantCounterAnimation = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    restaurantCounterContainer.setVisibility(View.VISIBLE);
                    restaurantCounterAnimation = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            restaurantCounterContainer.startAnimation(slide);
        }
    }

    private void hideRestaurantCounter() {
        if (restaurantCounterContainer != null && restaurantCounterContainer.getVisibility() == View.VISIBLE
                && !restaurantCounterAnimation) {
            Animation slide = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_out_bottom);
            slide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    restaurantCounterContainer.setVisibility(View.GONE);
                    restaurantCounterAnimation = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    restaurantCounterAnimation = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            restaurantCounterContainer.startAnimation(slide);
        }
    }

    private void showRestaurantList(List<Restaurant> restaurantList) {
        Log.i(TAG, String.valueOf(this.hashCode()));
        System.out.println(restaurantList);
        System.out.println(getView());

        if (restaurantList == null) return;

        try {
            if (restaurantListView != null && getActivity() != null) {
                RestaurantListAdapter rla=new RestaurantListAdapter(getActivity(), restaurantList);
                restaurantListView.setAdapter(rla);
                restaurantCounter.setText(getResources().getString(R.string.restaurant_found_counter, restaurantList.size()));
            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lastViewedRestaurantPosition!=null && getView()!=null) {
            ListView restaurantListView=(ListView)getView().findViewById(R.id.restaurant_list);
            final RestaurantListAdapter adapter=(RestaurantListAdapter)restaurantListView.getAdapter();

            final Restaurant prevRestaurant = adapter.getItem(lastViewedRestaurantPosition);

            AsyncTask<Void, Void, Restaurant> task = new AsyncTask<Void, Void, Restaurant>() {

                @Override
                protected Restaurant doInBackground(Void... voids) {
                    return new RestaurantManager().getRestaurantById(prevRestaurant.getId());
                }

                @Override
                protected void onPostExecute(Restaurant restaurant) {
                    adapter.remove(prevRestaurant);
                    adapter.insert(restaurant, lastViewedRestaurantPosition);
                    lastViewedRestaurantPosition = null;
                }
            };
            task.execute();
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
                listViewContainerFrame.setVisibility(View.GONE);
            }else {
                loadingLayout.setVisibility(View.GONE);
                listViewContainerFrame.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    private void showEmptyListMessage(boolean show) {
        if (show) {
            restaurantListViewPanel.setVisibility(View.GONE);
            emptyRestaurantListFrame.setVisibility(View.VISIBLE);
        } else {
            restaurantListViewPanel.setVisibility(View.VISIBLE);
            emptyRestaurantListFrame.setVisibility(View.GONE);
        }
    }

    public void refreshRestaurantList() {
        new AsyncTask<Void, Void, List<Restaurant>>() {

            @Override
            protected void onPreExecute() {
                showLoading(true);
            }

            @Override
            protected List<Restaurant> doInBackground(Void... params) {
                RestaurantsUpdaterTask task = RestaurantsUpdaterTask.getInstance(getActivity());
                task.startIfNecessary();

                try {
                    task.join(Const.MAX_UPDATING_MILLISECONDS_DELAY);
                } catch (InterruptedException ie) {
                    ExceptionUtil.handleException(ie);
                }

                return new RestaurantManager().queryRestaurantList(getRestaurantNameQuery(),
                        getRestaurantListFilter());
            }

            @Override
            protected void onPostExecute(List<Restaurant> restaurantList) {
                showLoading(false);
                if (restaurantList != null && restaurantList.size() > 0) {
                    showEmptyListMessage(false);
                    showRestaurantList(restaurantList);
                } else {
                    showEmptyListMessage(true);
                }
            }
        }.execute();
    }

}
