package com.jasoftsolutions.mikhuna.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jasoftsolutions.mikhuna.R;
import com.jasoftsolutions.mikhuna.activity.preferences.UserPreferences;
import com.jasoftsolutions.mikhuna.model.User;
import com.jasoftsolutions.mikhuna.remote.UserRemote;
import com.jasoftsolutions.mikhuna.util.EncryptUtil;
import com.jasoftsolutions.mikhuna.util.ExceptionUtil;
import com.jasoftsolutions.mikhuna.util.ValidationUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hugo on 26/03/2015.
 */
public class RegisterActivity extends BaseActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText mNameEdt, mLastNameEdt,
            mEmailEdt, mBirthdayEdt, mPasswordEdt, mConfirmPasswordEdt;

    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 30;

    private Button mBtnRegister;

    private int year, month, day;
    private String lastDate;

    private RadioGroup mRadioGroup;
    private RadioButton mRadioMale;
    private User mUser;

    private ProgressDialog mProgressDialog;

    public RegisterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameEdt = (EditText) findViewById(R.id.name);
        mLastNameEdt = (EditText) findViewById(R.id.last_name);
        mEmailEdt = (EditText) findViewById(R.id.email);
        mBirthdayEdt = (EditText) findViewById(R.id.birthday);
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group_sex);
        mPasswordEdt = (EditText) findViewById(R.id.password);
        mConfirmPasswordEdt = (EditText) findViewById(R.id.password_confirm);
        mBtnRegister = (Button) findViewById(R.id.btn_register_user);

        mBirthdayEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        String names, last_name, email, birthday, password, passwordConfirm, sex;
        names = mNameEdt.getText().toString();
        last_name = mLastNameEdt.getText().toString();
        email = mEmailEdt.getText().toString();
        birthday = mBirthdayEdt.getText().toString();
        password = mPasswordEdt.getText().toString();
        passwordConfirm = mConfirmPasswordEdt.getText().toString();
        sex = mRadioGroup.getCheckedRadioButtonId() == R.id.sex_male?"male":"female";



        if (validateForm(names, last_name, email, birthday, password, passwordConfirm, sex)){
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            mUser = new User(names, last_name, email, sex, birthday, EncryptUtil.toSHA1(password));
            try {
                mUser.setBirthday(df.parse(birthday));
            } catch (ParseException e) {
                ExceptionUtil.ignoreException(e);
            }
            mUser.setLoginType(User.OWN_LOGIN);
            new SaveUserAsyncTask().execute();
        }

    }

    private boolean validateForm(String names, String last_name,
                                 String email, String birthday, String password,
                                 String passwordConfirm, String sex) {

        if (!ValidationUtil.isNotBlankTexts(names, last_name, email, birthday, password, passwordConfirm, sex)){
            Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!ValidationUtil.isValidEmail(email)){
            Toast.makeText(this, "Por favor ingrese una dirección email válida", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!ValidationUtil.isAlphanumericText(password)){
            Toast.makeText(this, "La contraseña solo puede tener letras y números", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!ValidationUtil.isTextLengthBetween(password, MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH)){
            Toast.makeText(this,
                    "La contraseña debe tener entre " + MIN_PASSWORD_LENGTH + " y " + MAX_PASSWORD_LENGTH + " caracteres",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!ValidationUtil.isPasswordsEquals(password, passwordConfirm)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    lastDate = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                    mBirthdayEdt.setText(lastDate);
                }
            };

    private void showDialog(){
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null){
                ft.remove(prev);
            }

            ft.addToBackStack(null);
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, mDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mBirthdayEdt.setText(lastDate!=null?lastDate:"");
                }
            });
            dialog.show();
        } catch (Exception e){

        }

    }

    private class SaveUserAsyncTask extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(RegisterActivity.this, null, "Registrando ...");
            try {
                mProgressDialog.show();
            } catch (Exception e){
                ExceptionUtil.ignoreException(e);
            }
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            return new UserRemote().saveUser(mUser);
        }

        @Override
        protected void onPostExecute(Boolean response) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();

            if (response!=null){

                if (response){
                    UserPreferences userPreferences = new UserPreferences(RegisterActivity.this);
                    userPreferences.saveUser(mUser);
                    Toast.makeText(RegisterActivity.this, "Registro exitoso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, RestaurantListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, "El email ya esta registrado", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterActivity.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
