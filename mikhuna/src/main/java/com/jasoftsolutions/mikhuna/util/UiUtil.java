package com.jasoftsolutions.mikhuna.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.Tags;
import com.jasoftsolutions.mikhuna.activity.fragment.DefaultLoadingFragment;
import com.jasoftsolutions.mikhuna.domain.SelectOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc07 on 05/05/2014.
 */
public class UiUtil {

    /**
     * Funciona solamente con spinners que usen un <code>ArrayAdapter<SelectOption></code>
     * @param spinner
     * @param id
     */
    public static boolean selectSpinnerItemByOptionId(Spinner spinner, long id) {
        try {
            for (int i=0; i<spinner.getAdapter().getCount(); i++) {
                SelectOption opt = (SelectOption)spinner.getAdapter().getItem(i);
                if (opt.getId() == null) continue;

                if (opt.getId().equals(id)) {
                    spinner.setSelection(i, true);
                    return true;
                }
            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
        return false;
    }

    public static void initializeSpinnerDataAdapter(Spinner spinner, ArrayList<SelectOption> data) {
        ArrayAdapter<SelectOption> adapter =
                new ArrayAdapter<SelectOption>(spinner.getContext(),
                        android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    public static void addLoadingFragmentOnTransaction(FragmentTransaction ft) {
        ft.add(R.id.container, new DefaultLoadingFragment(), Tags.FRAGMENT_LOADING);
    }

    public static void addLoadingFragment(FragmentActivity fa) {
        try {
            FragmentManager fm = fa.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            addLoadingFragmentOnTransaction(ft);
            ft.commit();
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    public static void removeFragmentByTagIfExistsOnTransaction(FragmentManager fm, FragmentTransaction ft, String tag) {
        Fragment f = fm.findFragmentByTag(tag);
        if (f != null) {
            ft.remove(f);
        }
    }

    public static void removeLoadingFragmentIfExistsOnTransaction(FragmentManager fm, FragmentTransaction ft) {
        removeFragmentByTagIfExistsOnTransaction(fm, ft, Tags.FRAGMENT_LOADING);
    }

    public static void removeAllFragmentsOnTransaction(FragmentManager fm, FragmentTransaction ft) {
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment f : fm.getFragments()) {
                ft.remove(f);
            }
        }
    }

    public static void removeAllFragmentsAndAddLoadingFragment(FragmentActivity fa) {
        try {
            FragmentManager fm = fa.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            removeAllFragmentsOnTransaction(fm, ft);
            addLoadingFragmentOnTransaction(ft);
            ft.commit();
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }
}
