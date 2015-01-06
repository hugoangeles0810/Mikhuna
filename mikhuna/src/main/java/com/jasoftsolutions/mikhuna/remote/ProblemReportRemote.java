package com.jasoftsolutions.mikhuna.remote;

import com.jasoftsolutions.mikhuna.domain.ApplicationProblem;
import com.jasoftsolutions.mikhuna.remote.json.JsonResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc07 on 07/05/2014.
 */
public class ProblemReportRemote {

    public boolean sendProblemReport(ApplicationProblem problem) {
        RemoteHandler remoteHandler = new RemoteHandler();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (problem.getRestaurantServerId() != 0) {
            params.add(new BasicNameValuePair("rid", String.valueOf(problem.getRestaurantServerId())));
        }
        params.add(new BasicNameValuePair("pid", String.valueOf(problem.getProblemTypeId())));
        params.add(new BasicNameValuePair("d", String.valueOf(problem.getDetail())));
        params.add(new BasicNameValuePair("u", problem.getUser()));

        JsonResponse response = remoteHandler.postResourceFromUrl("/problem", params, JsonResponse.class);

        if (response != null && response.getSuccess()) {
            return true;
        } else {
            return false;
        }
    }

}
