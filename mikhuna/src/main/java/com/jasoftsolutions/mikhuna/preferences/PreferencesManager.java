package com.jasoftsolutions.mikhuna.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
	
	private Context context;

	public PreferencesManager(Context context) {
		this.context = context;
	}
	
	protected Context getContext() {
		return this.context;
	}
	
	public SharedPreferences getPreferences(String name) {
		return this.context.getSharedPreferences(name, Context.MODE_PRIVATE); 		
	}
	
}
