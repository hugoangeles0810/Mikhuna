package com.jasoftsolutions.mikhuna.activity.listener;

import android.content.Context;
import android.view.View;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;
import com.jasoftsolutions.mikhuna.util.PhoneCall;

/**
 * Created by pc07 on 27/03/14.
 */
public class CallButtonListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        Context context=view.getContext();
        Restaurant restaurant=(Restaurant)view.getTag(R.id.tag_restaurant);

        new AuditHelper(view.getContext()).registerDialIntentOf(restaurant);
        AnalyticsUtil.registerEvent(view.getContext(), AnalyticsConst.Category.DETAIL_RESTAURANT,
                AnalyticsConst.Action.DIAL_INTENT, restaurant.getServerId().toString());

        PhoneCall.makeDialCall(context, restaurant.getPhoneNumber());
    }
}