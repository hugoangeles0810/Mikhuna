package com.jasoftsolutions.mikhuna.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.fragment.ProblemReportFragment;

public class ProblemReportActivity extends BaseActivity {

    private static final String TAG = ProblemReportActivity.class.getSimpleName();

    private ProblemReportFragment problemReportFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_report);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);

        addProblemReportFragment();
    }

    private void addProblemReportFragment() {
        problemReportFragment = new ProblemReportFragment();
        problemReportFragment.setArguments(getIntent().getExtras());

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.container, problemReportFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
