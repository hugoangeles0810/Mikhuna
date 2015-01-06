package com.jasoftsolutions.mikhuna.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.ArgKeys;
import com.jasoftsolutions.mikhuna.activity.preferences.PersonalRestaurantRatingPreferences;
import com.jasoftsolutions.mikhuna.remote.Const;

/**
 * Created by pc07 on 28/03/14.
 */
public class RestaurantRatingFragment extends Fragment {

    private static final String TAG = RestaurantRatingFragment.class.getSimpleName();

    private RatingBar ratingBar1, ratingBar2, ratingBar3;
    private EditText restaurantRatingCommentEditText;
    private Button restaurantRatingSubmitButton;

    private int restaurantServerId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_problem_report, container, false);

        ratingBar1 = (RatingBar) rootView.findViewById(R.id.restaurant_rating_bar_1);
        ratingBar1.setMax(Const.MAX_RATING_BAR_VALUE);

        ratingBar2 = (RatingBar) rootView.findViewById(R.id.restaurant_rating_bar_2);
        ratingBar2.setMax(Const.MAX_RATING_BAR_VALUE);

        ratingBar3 = (RatingBar) rootView.findViewById(R.id.restaurant_rating_bar_3);
        ratingBar3.setMax(Const.MAX_RATING_BAR_VALUE);

        restaurantRatingCommentEditText = (EditText) rootView.findViewById(R.id.restaurant_rating_comment);
        restaurantRatingSubmitButton = (Button) rootView.findViewById(R.id.restaurant_rating_submit);

        if (savedInstanceState != null) {
            loadView(savedInstanceState);
        } else {
            loadView(getArguments());
        }

        loadPersonalRating();

        return rootView;
    }

    private void loadView(Bundle data) {
        if (data != null) {
            restaurantServerId = data.getInt(ArgKeys.RESTAURANT_SERVER_ID);
        }
        restaurantRatingSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void loadPersonalRating() {
        PersonalRestaurantRatingPreferences pref =
                new PersonalRestaurantRatingPreferences(getActivity(), restaurantServerId);

        ratingBar1.setProgress(pref.getRating1());
        ratingBar2.setProgress(pref.getRating2());
        ratingBar3.setProgress(pref.getRating3());

        restaurantRatingCommentEditText.setText(pref.getComment());
    }

//    private void sendReport() {
//        SendProblemReportThread.clearInstance();
//
//        ApplicationProblem problem = new ApplicationProblem();
//        if (restaurant != null) {
//            problem.setRestaurantServerId(restaurant.getServerId());
//        }
//        problem.setProblemTypeId(((SelectOption)problemReportType.getSelectedItem()).getId().intValue());
//        problem.setDetail(problemReportDescription.getText().toString());
//        problem.setUser(AccountUtil.getDefaultGoogleAccount(getActivity()));
//
//        SendProblemReportTask task = new SendProblemReportTask();
//        task.execute(problem);
//    }
//
//    private class SendProblemReportTask extends AsyncTask<ApplicationProblem, Void, Boolean> {
//
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(getActivity(),
//                    getString(R.string.msg_wait_momment_please), getString(R.string.msg_sending_progress),
//                    true, false);
//        }
//
//        @Override
//        protected Boolean doInBackground(ApplicationProblem... params) {
//            SendProblemReportThread sendProblemReportThread = SendProblemReportThread.getInstance(getActivity(), params[0]);
//            sendProblemReportThread.startIfNecessary();
//
//            try {
//                sendProblemReportThread.join(Const.MAX_UPDATING_MILLISECONDS_DELAY);
//                if (sendProblemReportThread.getResult() == null
//                        || !sendProblemReportThread.getResult()) {
//                    return false;
//                } else {
//                    return true;
//                }
//            } catch (InterruptedException e) {
//                ExceptionUtil.handleException(e);
//            } catch (Exception e) {
//                ExceptionUtil.handleException(e);
//            }
//
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean success) {
//            progressDialog.dismiss();
//            if (success) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//                builder.setTitle(R.string.title_activity_problem_report);
//                builder.setMessage(R.string.msg_successful_problem_report_sending);
//
//                builder.setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        getActivity().finish();
//                    }
//                });
//                builder.setCancelable(true);
//                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        getActivity().finish();
//                    }
//                });
//
//                builder.create().show();
//
//            } else {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setCancelable(false);
//
//                builder.setTitle(R.string.title_activity_problem_report);
//                builder.setMessage(R.string.msg_fail_problem_report_sending);
//
//                builder.setPositiveButton(R.string.dialog_button_retry, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        sendReport();
//                    }
//                });
//                builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // dismiss
//                    }
//                });
//
//                builder.create().show();
//            }
//        }
//    }
}
