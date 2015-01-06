package com.jasoftsolutions.mikhuna.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class CredentialsManager extends PreferencesManager {
	
	private static final String PREFERENCES_CREDENTIALS = "credentials";
	
	public CredentialsManager(Context context) {
		super(context);
	}
	
	private SharedPreferences getCredentialsPreferences() {
		return getPreferences(PREFERENCES_CREDENTIALS);
	}
	
	public String getAccessToken() {
		return getCredentialsPreferences().getString("access_token", null);
	}
	
//	public String getRefreshToken
	
}
