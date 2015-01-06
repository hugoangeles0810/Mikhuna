package com.jasoftsolutions.mikhuna.security;

import com.jasoftsolutions.mikhuna.preferences.PreferencesManager;
import com.jasoftsolutions.mikhuna.security.exception.NoAuthenticatedException;
import com.jasoftsolutions.mikhuna.security.exception.UserDoesNotExistsException;

import android.content.Context;
import android.content.SharedPreferences;

public class Security extends PreferencesManager {
	
	public static final String PREFERENCES_CREDENTIALS = "credentials";
	
	private static Security singletonInstance;
	
	private Security(Context context) {
		super(context);
	}
		
	public static Security create(Context context, boolean force) {
		if (singletonInstance == null || force) {
			singletonInstance=new Security(context);
		}
		return singletonInstance;
	}
	
	/**
	 * Crea la clase de seguridad por defecto sólo si ésta no ha sido creada aún
	 * @param context
	 * @return {@link Security}
	 */
	public static Security create(Context context) {
		return create(context, false);
	}

	public static Security getCurrentSecurity() {
		return singletonInstance;
	}
	
	
	
	/// Métodos de seguridad
	

	public SharedPreferences getCredentialsPreferences() {
		return getPreferences(PREFERENCES_CREDENTIALS);
	}

	public String getAccessToken() {
		String access_token = getCredentialsPreferences().getString("access_token", null);
		if (access_token == null) throw new NoAuthenticatedException();
		return access_token;
	}
	
	public String getEmail() {
		return getCredentialsPreferences().getString("email", null);
	}
	
	public void authenticate(String email, String password) {
		throw new UserDoesNotExistsException();
	}
	
	public void signUp(String email, String password, String firstName) {
		SharedPreferences csp=getCredentialsPreferences();
		csp.edit()
			.putString("email", email)
			.putString("access_token", "1234567890987654321")
			.commit()
		;
	}
	
	public void cleanCredentials() {
		SharedPreferences csp=getCredentialsPreferences();
		csp.edit().clear().commit();
	}
	
}
