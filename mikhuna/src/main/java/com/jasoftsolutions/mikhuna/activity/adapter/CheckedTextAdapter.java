package com.jasoftsolutions.mikhuna.activity.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.domain.SelectOption;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pc07 on 29/04/2014.
 */
public class CheckedTextAdapter extends BaseAdapter {

    private Context context;
    private List<SelectOption> entries;
    private Map<Integer, Boolean> checkedMap;

    private CheckBoxListener checkBoxListener;

    public CheckedTextAdapter(Context context) {
        this.context = context;
        checkBoxListener = new CheckBoxListener();
    }

    public List<SelectOption> getEntries() {
        return entries;
    }

    public void setEntries(List<SelectOption> entries) {
        this.entries = entries;
        checkedMap = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    public boolean isChecked(int position) {
        Boolean result = checkedMap.get(position);
        if (result == null) {
            return false;
        } else {
            return result;
        }
    }

    public void setChecked(int position, boolean checked) {
        checkedMap.put(position, checked);
        notifyDataSetChanged();
    }

    public void setCheckedListIds(List<Long> ids) {
        for (long id : ids) {
            setCheckedById(id, true);
        }
    }

    /**
     *
     * @param id
     * @param checked
     * @return False si no se encuentra el id indicado
     */
    public boolean setCheckedById(long id, boolean checked) {
        for (int i=0; i<entries.size(); i++) {
            SelectOption opt = entries.get(i);

            if (opt.getId() == null) continue;
            if (opt.getId().equals(id)) {
                setChecked(i, checked);
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCount() {
        if (entries == null) return 0;
        else return entries.size();
    }

    @Override
    public Object getItem(int i) {
        return entries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CheckBox checkBox;
        if (view == null) {
            checkBox = new CheckBox(context);
            checkBox.setOnCheckedChangeListener(checkBoxListener);
        } else {
            checkBox = (CheckBox)view;
        }
        checkBox.setTag(R.id.tag_list_position, i);
        checkBox.setText(entries.get(i).toString());
        checkBox.setChecked(isChecked(i));
        return checkBox;
    }

    private class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (Integer)buttonView.getTag(R.id.tag_list_position);
            checkedMap.put(position, isChecked);
        }
    }
}
