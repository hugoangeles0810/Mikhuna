package com.jasoftsolutions.mikhuna.remote;

import android.content.Context;

import com.jasoftsolutions.mikhuna.domain.ApplicationProblem;

/**
 * Created by pc07 on 07/05/2014.
 */
public class SendProblemReportThread extends Thread {

    private static final String THREAD_NAME = SendProblemReportThread.class.getName();
    private static final String TAG = SendProblemReportThread.class.getSimpleName();

    private Context context;

    private ApplicationProblem applicationProblem;

    private static SendProblemReportThread instance;

    private Boolean result;

    private SendProblemReportThread(Context context, ApplicationProblem problem) {
        super(THREAD_NAME);
        this.context = context;
        this.applicationProblem = problem;
    }

    public static SendProblemReportThread getInstance(Context context, ApplicationProblem problem) {
        if (instance==null || !instance.isAlive()) {
            instance = new SendProblemReportThread(context, problem);
        }
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }

    public void startIfNecessary() {
        if (!isAlive()) start();
    }

    @Override
    public synchronized void run() {
        result = null;

        ProblemReportRemote service = new ProblemReportRemote();

        result = service.sendProblemReport(applicationProblem);

        instance=null;
    }

    public Boolean getResult() {
        return result;
    }
}
