package com.jasoftsolutions.mikhuna.remote;

import android.content.Context;
import android.util.Log;

import com.jasoftsolutions.mikhuna.data.AuditManager;
import com.jasoftsolutions.mikhuna.model.Audit;
import com.jasoftsolutions.mikhuna.remote.json.JsonResponse;
import com.jasoftsolutions.mikhuna.util.AccountUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc07 on 08/04/2014.
 */
public class AuditRemote {

    private static final String TAG = AuditRemote.class.getSimpleName();

    private Context context;

    public AuditRemote(Context context) {
        this.context = context;
    }

    public void sendAudit() {
        AuditManager am=new AuditManager();

        String userAccount=AccountUtil.getDefaultGoogleAccount(context);
        while (true) {
            List<Audit> audits=am.getPendingAudits(Const.MAX_AUDITS_X_POST);
            if (audits==null || audits.size()==0) break;
            if (!doPost(audits, userAccount)) break;
            am.markSentTo(audits);
        }

    }

    private boolean doPost(List<Audit> audits, String userAccount) {
        if (audits==null || audits.size()==0) return false;

        RemoteHandler sh=new RemoteHandler();

        List<NameValuePair> params=new ArrayList<NameValuePair>();
        JSONArray data=new JSONArray();

        for (Audit a : audits) {
            try {
                JSONObject obj=new JSONObject();
                obj.put("aid", a.getActionId());
                obj.put("d", a.getActionDate());
                obj.put("rid", a.getRestaurantServerId());

                data.put(obj);
            } catch (JSONException e) {
                ExceptionUtil.ignoreException(e);
            }
        }
        params.add(new BasicNameValuePair("data", data.toString()));
        params.add(new BasicNameValuePair("user", userAccount));

        JsonResponse response = sh.postResourceFromUrl("/audit/", params, JsonResponse.class);
        Log.i(TAG, "data="+data.toString());

        if (response!=null && response.getSuccess()) {
            return true;
        }

        return false;
    }

}
