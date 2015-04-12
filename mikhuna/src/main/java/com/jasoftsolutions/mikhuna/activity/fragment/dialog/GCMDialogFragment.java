package com.jasoftsolutions.mikhuna.activity.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.ArgKeys;
import com.jasoftsolutions.mikhuna.activity.util.RestaurantViewUtil;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.util.TriggerQueue;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Hugo on 11/04/2015.
 */
public class GCMDialogFragment extends DialogFragment {

    public static final String TAG = GCMDialogFragment.class.getSimpleName();

    private TriggerQueue triggerQueue;

    public static GCMDialogFragment newInstance(Bundle args){
        GCMDialogFragment df = new GCMDialogFragment();
        if (args != null){
            df.setArguments(args);
        }
        return  df;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.AlertDialogCustom);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_gcm, container);
        ImageView imageView;
        TextView description;
        TextView message;

        rootView.findViewById(R.id.button_ok).setOnClickListener(listenerBtn);

        if (getArguments() != null){
            imageView = (ImageView) rootView.findViewById(R.id.dialog_gcm_photo);
            description = (TextView) rootView.findViewById(R.id.dialog_gcm_description);

            if (getArguments().containsKey(ArgKeys.RESTAURANT_ID)){
                Restaurant restaurant = new RestaurantManager().getRestaurantByServerId(getArguments().getLong(ArgKeys.RESTAURANT_ID));
                RestaurantViewUtil.displayRestaurantImage(restaurant, imageView);
            }

            if (getArguments().containsKey(ArgKeys.PHOTO)){
                String url = Const.BACKEND_BASE_URL + getArguments().getString(ArgKeys.PHOTO);
                ImageLoader.getInstance().displayImage(url, imageView);
            }

            if (getArguments().containsKey(ArgKeys.MESSAGE)){
                String desc = getArguments().getString(ArgKeys.MESSAGE);
                description.setText(desc);
            }

        }
        return rootView;
    }

    View.OnClickListener listenerBtn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (triggerQueue != null){
                triggerQueue.completeOne();
            }
        }
    };

    public void setTriggerQueue(TriggerQueue triggerQueue) {
        this.triggerQueue = triggerQueue;
    }
}
