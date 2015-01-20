package com.jasoftsolutions.mikhuna.activity.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.model.RestaurantDish;
import com.jasoftsolutions.mikhuna.model.RestaurantDishCategory;
import com.jasoftsolutions.mikhuna.model.RestaurantDishPresentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo on 11/01/2015.
 */
public class DishCategoryAdapter extends BaseExpandableListAdapter {

    public static final String TAG = DishCategoryAdapter.class.getSimpleName();

    private Long restaurantServerId;
    private ArrayList<RestaurantDishCategory> dishCategories;
    private Context context;
    private LayoutInflater inflater;

    public DishCategoryAdapter(Context context, ArrayList<RestaurantDishCategory> dishCategories) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.dishCategories = dishCategories;
        getDishByCategory(dishCategories);

    }


    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    private void getDishByCategory(ArrayList<RestaurantDishCategory> dishCategories) {
        RestaurantManager rm = new RestaurantManager();
        for (RestaurantDishCategory rdc : dishCategories){
            rdc.setRestaurantDishes(rm.getRestaurantDishesOf(rdc.getServerId()));
            for (RestaurantDish rd : rdc.getRestaurantDishes()){
                rd.setDishPresentations(rm.getDishPresentationsOf(rd.getServerId()));
            }
        }
    }

    @Override
    public int getGroupCount() {
        return dishCategories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dishCategories.get(groupPosition).getRestaurantDishes().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dishCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dishCategories.get(groupPosition).getRestaurantDishes().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return ((RestaurantDishCategory)getGroup(groupPosition)).getServerId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return ((RestaurantDish)getChild(groupPosition, childPosition)).getServerId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolderGroup holder;

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_item_category, null);

            holder = new ViewHolderGroup();
            holder.categoryName = (TextView)view.findViewById(R.id.list_item_category_name);
            holder.categoryDescription = (TextView)view.findViewById((R.id.list_item_category_description));

            view.setTag(holder);
        }else{
            holder = (ViewHolderGroup)view.getTag();
        }

        RestaurantDishCategory rdc = (RestaurantDishCategory) getGroup(groupPosition);

        holder.categoryName.setText(rdc.getName());

        if (rdc.getDescription()!=null){
            holder.categoryDescription.setText(rdc.getDescription());
            holder.categoryDescription.setVisibility(View.VISIBLE);
        }else{
            holder.categoryDescription.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolderChild holder;
        RestaurantDish dish = (RestaurantDish) getChild(groupPosition, childPosition);
        if (view == null){
            view = inflater.inflate(R.layout.fragment_item_dish, null);

            holder = new ViewHolderChild();
            holder.dishName = (TextView)view.findViewById(R.id.tv_dish_name);
            holder.dishPrice = (TextView)view.findViewById(R.id.tv_dish_price);
            holder.dishDescription = (TextView)view.findViewById(R.id.tv_dish_description);
            holder.listPresentations = (LinearLayout)view.findViewById(R.id.list_presentations);

            view.setTag(holder);
        }else{
            holder = (ViewHolderChild)view.getTag();
        }

        holder.dishName.setText(dish.getName());

        if (dish.getPrice()!=null && dish.getPrice()>0){
            holder.dishPrice.setVisibility(View.VISIBLE);
            holder.dishPrice.setText(String.format("S/. %.1f", dish.getPrice()));
        }else{
            holder.dishPrice.setVisibility(View.GONE);
        }

        if(dish.getDescription()!=null){
            holder.dishDescription.setText(dish.getDescription());
            holder.dishDescription.setVisibility(View.VISIBLE);
        }else{
            holder.dishDescription.setVisibility(View.INVISIBLE);
        }


        if (dish.getDishPresentations()!=null && !dish.getDishPresentations().isEmpty()){
            holder.listPresentations.removeAllViews();
            setPresentationViewOnLinearLayout(holder.listPresentations, dish.getDishPresentations());
            holder.listPresentations.setVisibility(View.VISIBLE);
        }else{
            holder.listPresentations.setVisibility(View.GONE);
        }


        return view;
    }

    private void setPresentationViewOnLinearLayout(LinearLayout listPresentations, List<RestaurantDishPresentation> dishPresentations) {
        for (RestaurantDishPresentation presentation : dishPresentations){
            View view;
            view = inflater.inflate(R.layout.fragment_item_presentation, null);
            TextView name = (TextView) view.findViewById(R.id.tv_presentation_name);
            TextView price = (TextView) view.findViewById(R.id.tv_presentation_price);
            name.setText(presentation.getName());
            price.setText(String.format("S/. %.1f",presentation.getCost()));
            listPresentations.addView(view);
        }
    }

    private static final class ViewHolderGroup{
        TextView categoryName;
        TextView categoryDescription;
    }

    private static final class ViewHolderChild{
        TextView dishName;
        TextView dishPrice;
        TextView dishDescription;
        LinearLayout listPresentations;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
