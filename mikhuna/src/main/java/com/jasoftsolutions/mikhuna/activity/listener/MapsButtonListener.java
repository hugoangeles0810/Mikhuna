package com.jasoftsolutions.mikhuna.activity.listener;

import android.content.Context;
import android.view.View;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.MapActivity;
import com.jasoftsolutions.mikhuna.activity.fragment.dialog.Dialogs;
import com.jasoftsolutions.mikhuna.activity.util.AuditHelper;
import com.jasoftsolutions.mikhuna.model.Location;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.util.AnalyticsConst;
import com.jasoftsolutions.mikhuna.util.AnalyticsUtil;
import com.jasoftsolutions.mikhuna.util.InternetUtil;
import com.jasoftsolutions.mikhuna.util.MapsUtil;

/**
 * Created by pc07 on 28/03/14.
 */
public class MapsButtonListener implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        Context context=view.getContext();
        Restaurant restaurant=(Restaurant)view.getTag(R.id.tag_restaurant);
        Location location=new Location(restaurant.getLatitude(), restaurant.getLongitude(),
                restaurant.getName());

        new AuditHelper(view.getContext()).registerMapIntentOf(restaurant);
        AnalyticsUtil.registerEvent(view.getContext(), AnalyticsConst.Category.DETAIL_RESTAURANT,
                AnalyticsConst.Action.MAP_INTENT, restaurant.getServerId().toString());

        if (InternetUtil.isInternetConnected(context)){
            context.startActivity(MapActivity.getLauncherMapForDetailRestaurant(context, location));
        } else {
            Dialogs.dialogWithMessage(context, context.getString(R.string.no_internet_from_map_access)).show();
        }

    }
}