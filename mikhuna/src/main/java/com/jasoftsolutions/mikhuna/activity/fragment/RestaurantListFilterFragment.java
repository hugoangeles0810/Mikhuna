package com.jasoftsolutions.mikhuna.activity.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.adapter.CheckedTextAdapter;
import com.jasoftsolutions.mikhuna.activity.listener.ApplyActionListener;
import com.jasoftsolutions.mikhuna.activity.ui.ExpandedGridView;
import com.jasoftsolutions.mikhuna.data.ManagementManager;
import com.jasoftsolutions.mikhuna.domain.RestaurantListFilter;
import com.jasoftsolutions.mikhuna.domain.RestaurantServiceType;
import com.jasoftsolutions.mikhuna.domain.SelectOption;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc07 on 28/04/2014.
 */
public class RestaurantListFilterFragment extends Fragment {

    public static final String TAG = RestaurantListFilterFragment.class.getSimpleName();

    public static final String FROM_MAP = "from_map";

    private ManagementManager managementManager;

    private ApplyActionListener applyActionListener;

    private Spinner ubigeoSpinner;
    private TableLayout restaurantCategoryGridView;
    private TextView markAllRestaurantCategoriesTextView;
    private TextView unmarkAllRestaurantCategoriesTextView;
    private GridView serviceTypeGridView;
    private Button applyButton;
    private ArrayList<CheckBox> checkBoxesCategories;

    // Necesario para referenciar el adaptador del Grid personalizado
    private CheckedTextAdapter adapterCategoryRestaurant;

    private RestaurantListFilter currentFilter;

    private boolean emptyUbigeo;
    private boolean mFromMap = false;

    public static RestaurantListFilterFragment newInstance(boolean isFromMap){
        RestaurantListFilterFragment f = new RestaurantListFilterFragment();
        Bundle args = new Bundle();
        args.putBoolean(FROM_MAP, isFromMap);
        f.setArguments(args);
        return f;
    }



    public RestaurantListFilterFragment() {
        managementManager = new ManagementManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurant_list_filter, container, false);


        if (getArguments() != null) mFromMap = getArguments().getBoolean(FROM_MAP);

        if(mFromMap){
            rootView.findViewById(R.id.container_location).setVisibility(View.GONE);
        }

        ubigeoSpinner = (Spinner)rootView.findViewById(R.id.restaurant_list_filter_ubigeo);
        initializeUbigeoSpinner(ubigeoSpinner);

        restaurantCategoryGridView =(TableLayout)rootView.findViewById(R.id.restaurant_list_filter_restocat_grid);
        initializeRestaurantCategoryGridView(restaurantCategoryGridView);


        markAllRestaurantCategoriesTextView = (TextView)rootView.findViewById(R.id.restaurant_list_filter_mark_all_restocat_link);
        setupRestaurantCategoryMarkAllLink(markAllRestaurantCategoriesTextView);

        unmarkAllRestaurantCategoriesTextView = (TextView)rootView.findViewById(R.id.restaurant_list_filter_unmark_all_restocat_link);
        setupRestaurantCategoryUnMarkAllLink();

        serviceTypeGridView=(ExpandedGridView)rootView.findViewById(R.id.restaurant_list_filter_servicetype_grid);
        initializeServiceTypeGridView(serviceTypeGridView);

        applyButton = (Button)rootView.findViewById(R.id.button_apply_filter);
        initializeApplyButton();

        loadFromFilter();

        return rootView;
    }

    private void initializeApplyButton() {
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                computeFilter();

                if (mFromMap && currentFilter.getRestaurantCategories() == null
                        || currentFilter.getRestaurantCategories().size() == 0
                        || currentFilter.getServiceTypes() == null
                        || currentFilter.getServiceTypes().size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder
                            .setMessage(R.string.filter_map_empty_error_message)
                            .setPositiveButton(R.string.msg_ok, null)
                    ;
                    builder.create().show();

                }else if(currentFilter.getUbigeoServerId() == null
                        || currentFilter.getUbigeoServerId() == 0
                        || currentFilter.getRestaurantCategories() == null
                        || currentFilter.getRestaurantCategories().size() == 0
                        || currentFilter.getServiceTypes() == null
                        || currentFilter.getServiceTypes().size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder
                            .setMessage(R.string.filter_empty_error_message)
                            .setPositiveButton(R.string.msg_ok, null)
                    ;
                    builder.create().show();
                } else {
                    if (applyActionListener != null) {
                        applyActionListener.onApplyAction(RestaurantListFilterFragment.this);
                    }
                }

            }
        });
    }

    private void loadFromFilter() {
        if (currentFilter == null) return;

        if (currentFilter.getUbigeoServerId() != null) {
            UiUtil.selectSpinnerItemByOptionId(ubigeoSpinner, currentFilter.getUbigeoServerId());
        }

        if (currentFilter.getRestaurantCategories() != null && currentFilter.getRestaurantCategories().size() > 0) {
            for (CheckBox box : checkBoxesCategories){
                SelectOption so = (SelectOption) box.getTag();
                if (currentFilter.getRestaurantCategories().contains(so.getId())){
                    box.setChecked(true);
                }
            }
        }

        if (currentFilter.getServiceTypes() != null && currentFilter.getServiceTypes().size() > 0) {
            CheckedTextAdapter adapter = (CheckedTextAdapter)serviceTypeGridView.getAdapter();
            adapter.setCheckedListIds(currentFilter.getServiceTypes());
        }
    }

    private void setupRestaurantCategoryMarkAllLink(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupCheckBox(checkBoxesCategories, true);
            }
        });
    }

    private void setupCheckBox(List<CheckBox> boxes, boolean state){
        for (CheckBox box : boxes){
            box.setChecked(state);
        }
    }

    private void setupRestaurantCategoryUnMarkAllLink() {
        unmarkAllRestaurantCategoriesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupCheckBox(checkBoxesCategories, false);
            }
        });
    }

    private void initializeUbigeoSpinner(Spinner spinner) {
//        ArrayAdapter<SelectOption> adapter =
//                new ArrayAdapter<SelectOption>(getActivity(),
//                    android.R.layout.simple_spinner_item, managementManager.getUbigeosFrom(1L)); // todo quitar el número manualmente
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        spinner.setAdapter(adapter);
        UiUtil.initializeSpinnerDataAdapter(spinner, managementManager.getUbigeosFrom(1L));
        if (hasEmptyUbigeo()) {
            ArrayAdapter<SelectOption> adapter = (ArrayAdapter<SelectOption>)spinner.getAdapter();
            adapter.insert(new SelectOption(0L, ""), 0);
//            spinner.setSelection(-1);
        }
    }

    private void initializeRestaurantCategoryGridView(TableLayout gridLayout) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        checkBoxesCategories = new ArrayList<CheckBox>();
        TableRow.LayoutParams paramsCheck = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        int flag = 0;
        TableRow row = null;
        for (SelectOption option : managementManager.getAllRestaurantCategories()){
            if (flag == 0){
                row = new TableRow(getActivity());
                row.setGravity(Gravity.CENTER_VERTICAL);
            }

            flag++;

            CheckBox checkBox = new CheckBox(getActivity());
            checkBox.setLayoutParams(paramsCheck);
            checkBox.setText(option.getName());
            checkBox.setTag(option);
            row.addView(checkBox);
            checkBoxesCategories.add(checkBox);

            if (flag == 2){
                gridLayout.addView(row);
                flag = 0;
            }
        }

        if (flag == 1){
            gridLayout.addView(row);
        }
    }

    private void initializeServiceTypeGridView(GridView gridView) {
        CheckedTextAdapter adapter = new CheckedTextAdapter(getActivity());
        adapter.setEntries(RestaurantServiceType.getSelectOptions(getActivity()));
        gridView.setAdapter(adapter);
    }

    public void computeFilter() {
        RestaurantListFilter filter = new RestaurantListFilter();

        try {
            SelectOption so = (SelectOption)ubigeoSpinner.getSelectedItem();
            filter.setUbigeoServerId(so.getId());

            filter.setRestaurantCategories(getCheckedIn(checkBoxesCategories));

            filter.setServiceTypes(getCheckedIn((CheckedTextAdapter) serviceTypeGridView.getAdapter()));

            currentFilter = filter;
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
            currentFilter = null;
        }
    }

    private ArrayList<Long> getCheckedIn(CheckedTextAdapter adapter) {
        ArrayList<Long> result = new ArrayList<Long>();
        for (int i=0; i<adapter.getCount(); i++) {
            if (adapter.isChecked(i)) {
                result.add(((SelectOption) adapter.getItem(i)).getId());
            }
        }
        return result;
    }

    private ArrayList<Long> getCheckedIn(List<CheckBox> boxes){
        ArrayList<Long> result =  new ArrayList<Long>();
        for (CheckBox box : boxes){
            if (box.isChecked()){
                result.add(((SelectOption) box.getTag()).getId());
            }
        }

        return result;
    }

    public RestaurantListFilter getCurrentFilter() {
        return currentFilter;
    }

    public void setCurrentFilter(RestaurantListFilter currentFilter) {
        this.currentFilter = currentFilter;
    }

    public ApplyActionListener getApplyActionListener() {
        return applyActionListener;
    }

    public void setApplyActionListener(ApplyActionListener applyActionListener) {
        this.applyActionListener = applyActionListener;
    }

    public boolean hasEmptyUbigeo() {
        return emptyUbigeo;
    }

    public void setEmptyUbigeo(boolean emptyUbigeo) {
        this.emptyUbigeo = emptyUbigeo;
    }
}
