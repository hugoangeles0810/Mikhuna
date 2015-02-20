package com.jasoftsolutions.mikhuna.activity.fragment.dialog;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.MapActivity;
import com.jasoftsolutions.mikhuna.activity.Tags;
import com.jasoftsolutions.mikhuna.activity.fragment.RestaurantPreviewFragment;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.store.RestaurantLoader;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.UiUtil;

/**
 * Created by Hugo on 04/02/2015.
 */
public class RestaurantDialogFragment extends DialogFragment implements
        LoaderManager.LoaderCallbacks<Restaurant>{

    public static final String TAG = RestaurantDialogFragment.class.getSimpleName();

    private static String RESTAURANT_ID = "restaurant_id";
    private static int LOADER_RESTAURANT = 0;
    private long mServerId;

    private boolean isFromList = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.AlertDialogCustom);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_dialog, container);
       return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_RESTAURANT, null, this);
        UiUtil.removeAllFragmentsAndAddLoadingFragment(getChildFragmentManager());
    }

    public boolean isFromList() {
        return isFromList;
    }

    public void setFromList(boolean isFromList) {
        this.isFromList = isFromList;
    }

    public static RestaurantDialogFragment newInstance(long serverId){
        RestaurantDialogFragment rf = new RestaurantDialogFragment();
        Bundle args = new Bundle();
        args.putLong(RESTAURANT_ID, serverId);
        rf.setArguments(args);
        return  rf;
    }

    @Override
    public Loader<Restaurant> onCreateLoader(int id, Bundle args) {
        Log.d("Loader", "creando loader");
        return new RestaurantLoader(getActivity(), getArguments().getLong(RESTAURANT_ID));
    }

    @Override
    public void onLoadFinished(Loader<Restaurant> loader, Restaurant data) {
        try {
            if (data != null) {
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                RestaurantPreviewFragment rpf = (RestaurantPreviewFragment)
                        fm.findFragmentByTag(Tags.FRAGMENT_PREVIEW);
                if (rpf == null){
                    rpf = RestaurantPreviewFragment.newInstance(isFromList);
                    ft.replace(R.id.container, rpf, Tags.FRAGMENT_PREVIEW);
                }

                ft.commit();


                rpf.display(data);
            }else{
                Dialogs.noInternetConnectionMessage(getActivity()).show();
                getDialog().cancel();
            }
        }catch (Exception e){
            ExceptionUtil.handleException(e);
        }

    }

    @Override
    public void onLoaderReset(Loader<Restaurant> loader) {

    }
}
