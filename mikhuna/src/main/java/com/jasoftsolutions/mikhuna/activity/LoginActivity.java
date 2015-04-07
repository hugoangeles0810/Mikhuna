package com.jasoftsolutions.mikhuna.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.preferences.RestaurantListFilterPreferences;
import com.jasoftsolutions.mikhuna.activity.preferences.UserPreferences;
import com.jasoftsolutions.mikhuna.data.RestaurantManager;
import com.jasoftsolutions.mikhuna.model.User;
import com.jasoftsolutions.mikhuna.remote.UserRemote;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.UiUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends BaseActivity {

    private UiLifecycleHelper uiLifecycleHelper;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton authButton = (LoginButton) findViewById(R.id.fb_auth_button);
        authButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));

        uiLifecycleHelper = new UiLifecycleHelper(this, new SessionStatusCallback());
        uiLifecycleHelper.onCreate(savedInstanceState);

        Button btnNotNow = (Button) findViewById(R.id.btn_not_now);
        Button btnRegister = (Button) findViewById(R.id.btn_register);
        btnNotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processNextActivity();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        uiLifecycleHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiLifecycleHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiLifecycleHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiLifecycleHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
    }

    private void makeMeRequestProfile(final Session session) {



        final UserRemote userRemote = new UserRemote();

        try{
            progressDialog = ProgressDialog.show(LoginActivity.this,
                    null,
                    "Guardando perfil ...");
            progressDialog.show();
        }catch (Exception e){
            ExceptionUtil.ignoreException(e);
        }

        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {

                if (progressDialog != null  && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if (session == Session.getActiveSession()) {
                    if (user != null) {
                        if (session.isPermissionGranted("email") &&
                                session.isPermissionGranted("user_birthday")
                                && user.asMap().get("email") != null) {
                            final User usr = new User();
                            usr.setFirstname(user.getFirstName());
                            usr.setLastname(user.getLastName());
                            try {
                                usr.setBirthday((new SimpleDateFormat("MM/dd/yyyy")).parse(user.getBirthday()));
                            } catch (ParseException e) {
                                usr.setBirthday(null);
                            }
                            usr.setEmail(user.asMap().get("email").toString());
                            usr.setGender(user.asMap().get("gender").toString());
                            usr.setImgUrl(" https://graph.facebook.com/" + user.getId() + "/picture");
                            usr.setLoginType(User.FACEBOOK_LOGIN);
                            usr.setUid(user.getId());
                            Log.i("TAG", user.getInnerJSONObject().toString());
                            Log.i("TAG", usr.getFirstname() + " " + usr.getEmail() + " " + usr.getGender() + " " + usr.getImgUrl() + " " + usr.getLastname() + " " + usr.getBirthday().toString());

                            new AsyncTask<Void, Void, Boolean>(){
                                @Override
                                protected Boolean doInBackground(Void... params) {
                                    return userRemote.saveUser(usr);
                                }

                                @Override
                                protected void onPostExecute(Boolean response) {
                                    super.onPostExecute(response);
                                    if (response != null){
                                        new UserPreferences(LoginActivity.this).saveUser(usr);
                                        Toast.makeText(LoginActivity.this, "Logueado con exito!", Toast.LENGTH_SHORT).show();
                                        processNextActivity();
                                    } else {
                                        session.close();
                                        Toast.makeText(LoginActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }.execute();


                        } else {
                            session.close();
                            Toast.makeText(LoginActivity.this, "No se logro obtener tus datos completos", Toast.LENGTH_SHORT).show();
                        }


                    }
                }

                if (response.getError() != null) {
                    Toast.makeText(LoginActivity.this, "No se pudo conectar con Facebook", Toast.LENGTH_LONG).show();
                }
            }
        });

        request.executeAsync();
    }

    private void processNextActivity() {
        Intent intent;
        if (new RestaurantManager().countAllRestaurants() == 0
                || new RestaurantListFilterPreferences(LoginActivity.this).getUbigeoId() == 0) {
            intent = new Intent(LoginActivity.this, FirstTimeActivity.class);
        } else {
            intent = new Intent(LoginActivity.this, RestaurantListActivity.class);
        }

        startActivity(intent);
        finish();
    }

    private class SessionStatusCallback implements Session.StatusCallback {

        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (session.isOpened()) {
                makeMeRequestProfile(session);
            }
        }
    }


    public static Intent getIntentLauncher(Context context) {
        return new Intent(context, LoginActivity.class);
    }

}
