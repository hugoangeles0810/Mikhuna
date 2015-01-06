package com.jasoftsolutions.mikhuna.remote;

import com.jasoftsolutions.mikhuna.remote.json.ManagementListJsonResponse;

public class ManagementRemote {

    public ManagementListJsonResponse getManagementList() {
        return getManagementList(null);
    }

	public ManagementListJsonResponse getManagementList(Long lastUpdate) {
        ManagementListJsonResponse result;
		
		RemoteHandler sh= RemoteHandler.getInstance();

        result=sh.getResourceFromUrl("/maintenance/", lastUpdate, ManagementListJsonResponse.class);

		return result;
	}



}
