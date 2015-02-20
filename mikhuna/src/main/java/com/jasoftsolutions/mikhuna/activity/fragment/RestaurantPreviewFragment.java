package com.jasoftsolutions.mikhuna.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.RestaurantDetailActivity;
import com.jasoftsolutions.mikhuna.activity.RestaurantListActivity;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.activity.util.RestaurantViewUtil;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;
import com.jasoftsolutions.mikhuna.util.StringUtil;

/**
 * Created by Hugo on 04/02/2015.
 */
public class RestaurantPreviewFragment extends Fragment {

    public static final String IS_FROM_LIST = "is_from_list";

    private Restaurant mRestaurant;
    private Boolean mIsFromListActivity = false;

    public static RestaurantPreviewFragment newInstance(Bundle args){
        RestaurantPreviewFragment rpf = new RestaurantPreviewFragment();
        if (args != null){
            rpf.setArguments(args);
        }
        return  rpf;
    }

    public static RestaurantPreviewFragment newInstance(boolean isFromListActivity){
        RestaurantPreviewFragment rpf = new RestaurantPreviewFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_FROM_LIST, isFromListActivity);
        rpf.setArguments(args);
        return  rpf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_dialog, container, false);
        if (savedInstanceState == null) render(rootView);

        return rootView;
    }

    public void display(Restaurant r) {
        mRestaurant = r;
        refresh();
    }

    public void refresh() {
        View rootView = getView();
        if (rootView != null) {
            render(rootView);
        }
    }

    private void render(View rootView) {
        if( mRestaurant != null) {
            TextView titleTextView;
            TextView descriptionTextView;
            ImageView logoImageView;
            RatingBar ratingBar;
            View minAmountTextViewLayout;
            TextView minAmountTextView;
            TextView estimatedArrivalTimeTextView;
            View estimatedArrivalTimeTextViewLayout;
            TextView isOpenTextView;
            TextView serviceTypeTextView;
            Button buttonMoreInfo;
            Button buttonCancel;

            titleTextView=(TextView)rootView.findViewById(R.id.restaurant_list_item_title);
            descriptionTextView=(TextView)rootView.findViewById(R.id.restaurant_list_item_description);
            minAmountTextViewLayout=rootView.findViewById(R.id.restaurant_list_item_min_amount_layout);
            minAmountTextView=(TextView)rootView.findViewById(R.id.restaurant_list_item_min_amount);
            logoImageView=(ImageView)rootView.findViewById(R.id.restaurant_list_item_logo);
            ratingBar=(RatingBar)rootView.findViewById(R.id.restaurant_list_item_rating);
            estimatedArrivalTimeTextViewLayout=rootView.findViewById(R.id.restaurant_list_item_estimated_arrival_time_layout);
            estimatedArrivalTimeTextView=(TextView)rootView.findViewById(R.id.restaurant_list_item_estimated_arrival_time);
            isOpenTextView = (TextView)rootView.findViewById(R.id.restaurant_list_item_is_open);
            serviceTypeTextView = (TextView)rootView.findViewById(R.id.restaurant_list_item_service_type);
            buttonMoreInfo = (Button)rootView.findViewById(R.id.button_more_info);
            buttonCancel = (Button)rootView.findViewById(R.id.button_cancel);

            if (buttonMoreInfo.getTag(R.id.tag_restaurant_server_id) == null){
                buttonMoreInfo.setTag(R.id.tag_restaurant_server_id, mRestaurant.getServerId());
                buttonMoreInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Long serverId = (Long) v.getTag(R.id.tag_restaurant_server_id);
                        Context context = v.getContext();
                        Intent detailIntent = RestaurantDetailActivity
                                .getLauncherIntentByServerId(context, serverId);



                        if (getActivity() instanceof RestaurantListActivity){
                            ((RestaurantListActivity)getActivity()).setmShakeEnabled(true);
                            new AuditHelper(context).registerShowRecommended(serverId);
                            AnalyticsUtil.registerEvent(context, AnalyticsConst.Category.LIST_RESTAURANT,
                                    AnalyticsConst.Action.VIEW_RECOMMENDED, serverId.toString());
                        }else{
                            new AuditHelper(context).registerViewRestaurantFromMap(serverId);

                            AnalyticsUtil.registerEvent(context, AnalyticsConst.Category.MAP,
                                    AnalyticsConst.Action.VIEW_RESTAURANT_FROM_MAP, serverId.toString());
                        }

                        context.startActivity(detailIntent);
                        ((DialogFragment) getParentFragment()).dismiss();
                    }
                });
            }

            if (buttonCancel.getTag(R.id.tag_dialog_retry_server_connection) == null){
                buttonCancel.setTag(R.id.tag_dialog_retry_server_connection, mRestaurant.getServerId());
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity() instanceof RestaurantListActivity){
                            ((RestaurantListActivity)getActivity()).setmShakeEnabled(true);
                        }
                        ((DialogFragment) getParentFragment()).dismiss();
                    }
                });
            }

            titleTextView.setText(mRestaurant.getName());
            descriptionTextView.setText(mRestaurant.getDescription());

            float minAmount=0;
            if (mRestaurant.getMinAmount()!=null) {
                minAmount=mRestaurant.getMinAmount();
            }


            if (mRestaurant.isDelivery()) {
                estimatedArrivalTimeTextViewLayout.setVisibility(View.VISIBLE);
                minAmountTextViewLayout.setVisibility(View.VISIBLE);
                minAmountTextView.setText(StringUtil.currencyFormat(mRestaurant.getCurrency(), minAmount));
                estimatedArrivalTimeTextView.setText(RestaurantViewUtil.getStandardDeliveryTime(mRestaurant));
            } else {
                estimatedArrivalTimeTextViewLayout.setVisibility(View.GONE);
                minAmountTextViewLayout.setVisibility(View.INVISIBLE);
            }

            RestaurantViewUtil.displaySmallLogo(mRestaurant, logoImageView);
            RestaurantViewUtil.setRating(mRestaurant, ratingBar);

            RestaurantViewUtil.showRestaurantIsOpen(mRestaurant, isOpenTextView);
            RestaurantViewUtil.showRestaurantServiceType(mRestaurant, serviceTypeTextView);

            if (getArguments() != null){
                mIsFromListActivity = getArguments().getBoolean(IS_FROM_LIST);
            }

            if (mIsFromListActivity){
                rootView.findViewById(R.id.tv_recommend).setVisibility(View.VISIBLE);
            }
        }


    }
}
