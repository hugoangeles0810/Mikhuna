package com.jasoftsolutions.mikhuna.domain;

import android.content.Context;

import com.jasoftsolutions.mikhuna.R;

import java.util.ArrayList;

/**
 * Created by pc07 on 06/05/2014.
 */
public enum AppProblemType {

    APP_FAIL(1, R.string.problem_type_app_fail),
    INCORRECT_DATA(2, R.string.problem_type_incorrect_data),
    CITY_NOT_FOUND(3, R.string.problem_type_city_not_found),
    OTHER(4, R.string.problem_type_other);

    private int problemTypeId;
    private int stringResourceId;

    AppProblemType(int problemTypeId, int stringResourceId) {
        this.problemTypeId = problemTypeId;
        this.stringResourceId = stringResourceId;
    }

    public int getProblemTypeId() {
        return problemTypeId;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }

    public static ArrayList<SelectOption> getSelectOptions(Context context) {
        ArrayList<SelectOption> result = new ArrayList<SelectOption>();
        for (AppProblemType item : values()) {
            result.add(new SelectOption((long)item.getProblemTypeId(),
                    context.getString(item.getStringResourceId())));
        }
        return result;
    }
}
