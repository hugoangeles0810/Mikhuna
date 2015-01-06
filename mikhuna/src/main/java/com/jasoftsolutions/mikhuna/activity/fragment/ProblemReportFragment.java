package com.jasoftsolutions.mikhuna.activity.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.ArgKeys;
import com.jasoftsolutions.mikhuna.domain.AppProblemType;
import com.jasoftsolutions.mikhuna.domain.ApplicationProblem;
import com.jasoftsolutions.mikhuna.domain.SelectOption;
import com.jasoftsolutions.mikhuna.model.Restaurant;
import com.jasoftsolutions.mikhuna.remote.Const;
import com.jasoftsolutions.mikhuna.remote.SendProblemReportThread;
import com.jasoftsolutions.mikhuna.util.AccountUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.UiUtil;

/**
 * Created by pc07 on 28/03/14.
 */
public class ProblemReportFragment extends Fragment {

    private static final String TAG = ProblemReportFragment.class.getSimpleName();

    private LinearLayout restaurantDataLayout;
    private EditText restaurantName;
    private Spinner problemReportType;
    private EditText problemReportDescription;
    private Button submit;

    private Restaurant restaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_problem_report, container, false);

        restaurantDataLayout = (LinearLayout) rootView.findViewById(R.id.problem_report_restaurant_data_layout);
        restaurantName = (EditText)rootView.findViewById(R.id.problem_report_restaurant_name);
        problemReportType = (Spinner)rootView.findViewById(R.id.problem_report_type);
        problemReportDescription = (EditText)rootView.findViewById(R.id.problem_report_description);
        submit = (Button)rootView.findViewById(R.id.problem_report_submit);

        checkRestaurant(savedInstanceState);
        initializeProblemTypes();
        initializeSubmitButton();

        problemReportDescription.requestFocus();

        return rootView;
    }

    private void checkRestaurant(Bundle savedInstance) {
        restaurant = null;
        if (getArguments() != null) {
            restaurant = (Restaurant)getArguments().getSerializable(ArgKeys.RESTAURANT);
        } else if (savedInstance != null) {
            restaurant = (Restaurant)savedInstance.getSerializable(ArgKeys.RESTAURANT);
        }

        if (restaurant != null) {
            restaurantDataLayout.setVisibility(View.VISIBLE);
            restaurantName.setText(restaurant.getName());
        } else {
            restaurantDataLayout.setVisibility(View.GONE);
        }
    }

    private void initializeProblemTypes() {
        UiUtil.initializeSpinnerDataAdapter(problemReportType, AppProblemType.getSelectOptions(getActivity()));
//        problemReportType.requestFocus();
    }

    private void initializeSubmitButton() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport();
            }
        });
    }

    private void sendReport() {
        SendProblemReportThread.clearInstance();

        ApplicationProblem problem = new ApplicationProblem();
        if (restaurant != null) {
            problem.setRestaurantServerId(restaurant.getServerId());
        }
        problem.setProblemTypeId(((SelectOption)problemReportType.getSelectedItem()).getId().intValue());
        problem.setDetail(problemReportDescription.getText().toString());
        problem.setUser(AccountUtil.getDefaultGoogleAccount(getActivity()));

        SendProblemReportTask task = new SendProblemReportTask();
        task.execute(problem);
    }

    private class SendProblemReportTask extends AsyncTask<ApplicationProblem, Void, Boolean> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    getString(R.string.msg_wait_momment_please), getString(R.string.msg_sending_progress),
                    true, false);
        }

        @Override
        protected Boolean doInBackground(ApplicationProblem... params) {
            SendProblemReportThread sendProblemReportThread = SendProblemReportThread.getInstance(getActivity(), params[0]);
            sendProblemReportThread.startIfNecessary();

            try {
                sendProblemReportThread.join(Const.MAX_UPDATING_MILLISECONDS_DELAY);
                if (sendProblemReportThread.getResult() == null
                        || !sendProblemReportThread.getResult()) {
                    return false;
                } else {
                    return true;
                }
            } catch (InterruptedException e) {
                ExceptionUtil.handleException(e);
            } catch (Exception e) {
                ExceptionUtil.handleException(e);
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressDialog.dismiss();
            if (success) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(R.string.title_activity_problem_report);
                builder.setMessage(R.string.msg_successful_problem_report_sending);

                builder.setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                builder.setCancelable(true);
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        getActivity().finish();
                    }
                });

                builder.create().show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);

                builder.setTitle(R.string.title_activity_problem_report);
                builder.setMessage(R.string.msg_fail_problem_report_sending);

                builder.setPositiveButton(R.string.dialog_button_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendReport();
                    }
                });
                builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss
                    }
                });

                builder.create().show();
            }
        }
    }
}
