package com.jasoftsolutions.mikhuna.activity.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.model.RestaurantDishPresentation;

import java.util.List;

/**
 * Created by Hugo on 13/01/2015.
 */
public class DishPresentationAdapter extends ArrayAdapter<RestaurantDishPresentation> {

    private List<RestaurantDishPresentation> presentations;
    private LayoutInflater inflater;

    public DishPresentationAdapter(LayoutInflater inflater, List<RestaurantDishPresentation> objects){
        super(inflater.getContext(), R.layout.fragment_item_presentation, objects);
        presentations = objects;
        this.inflater = inflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null){
            view = inflater.inflate(R.layout.fragment_item_presentation, null);

            holder = new ViewHolder();
            holder.presentationName = (TextView)view.findViewById(R.id.tv_presentation_name);
            holder.presentationCost = (TextView)view.findViewById(R.id.tv_presentation_price);

            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        RestaurantDishPresentation p = presentations.get(position);
        holder.presentationName.setText(p.getName());
        holder.presentationCost.setText(String.format("S/.%.2f", p.getCost()));

        return view;

    }

    public List<RestaurantDishPresentation> getPresentations() {
        return presentations;
    }

    public void setPresentations(List<RestaurantDishPresentation> presentations) {
        this.presentations = presentations;
    }

    private static final class ViewHolder{
        TextView presentationName;
        TextView presentationCost;
    }
}
