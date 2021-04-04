package com.example.piston.view.user;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.viewmodel.RegisterActivityViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private RegisterActivityViewModel registerActivityViewModel;

    TextInputLayout username;
    TextInputLayout email;
    TextInputLayout pwd;
    TextInputLayout pwd2;
    TextInputLayout birthday;
    CheckBox tos;
    Button signUpBtn;

    Bundle mSavedInstanceState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerActivityViewModel = new ViewModelProvider(this).get(RegisterActivityViewModel.class);

        this.username = (TextInputLayout) findViewById(R.id.userText);
        this.email = findViewById(R.id.emailText);
        this.pwd = findViewById(R.id.pwdText);
        this.pwd2 = findViewById(R.id.pwd2Text);
        this.birthday = findViewById(R.id.bdayText);
        this.tos = findViewById(R.id.tosCheck);
        this.signUpBtn = findViewById(R.id.registerBtn);

        if(savedInstanceState != null){
            mSavedInstanceState = savedInstanceState;
        }

        registerActivityViewModel.getRegisterFormState().observe(this, registerFormState -> {
            if (registerFormState == null){
                return;
            }
            if (registerFormState.getEmptyUsernameError() != null) {
                username.setError(getString(registerFormState.getEmptyUsernameError()));
            }
            else if (registerFormState.getUsernameError() != null) {
                username.setError(getString(registerFormState.getUsernameError()));
            }
            if (registerFormState.getEmailError() != null) {
                email.setError(getString(registerFormState.getEmailError()));
            }
            else if (registerFormState.getEmailExistError() != null) {
                email.setError(getString(registerFormState.getEmailExistError()));
            }
            if (registerFormState.getEmptyPwdError() != null) {
                pwd.setError(getString(registerFormState.getEmptyPwdError()));
            }
            else if (registerFormState.getPasswordError() != null) {
                pwd.setError(getString(registerFormState.getPasswordError()));
            }
            if (registerFormState.getPassword2Error() != null) {
                pwd2.setError(getString(registerFormState.getPassword2Error()));
            }
            if (registerFormState.getIsBdayValidError() != null) {
                birthday.setError(getString(registerFormState.getIsBdayValidError()));
            }
        });

        registerActivityViewModel.getRegisterResult().observe(this, registerResult -> {
            if (registerResult == null) {
                return;
            }
            if (registerResult.getError() != null) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.verify_reg_field),
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            if (registerResult.getSuccess()){
                setResult(Activity.RESULT_OK);
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.reg_succesful),
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                onBackPressed();
            }
        });

        username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                username.setError(null);
                registerActivityViewModel.registerUsernameChanged(username.getEditText().getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });
        pwd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pwd.setError(null);
                registerActivityViewModel.registerPwd1Changed(pwd.getEditText().getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });
        pwd2.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pwd2.setError(null);
                registerActivityViewModel.registerPwd2Changed(pwd.getEditText().getText().toString(),
                        pwd2.getEditText().getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });
        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email.setError(null);
                registerActivityViewModel.registerEmailChanged(email.getEditText().getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });

        birthday.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                birthday.setError(null);
                registerActivityViewModel.registerBdayChanged(birthday.getEditText().getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });

        tos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                signUpBtn.setEnabled(showRegBtn());
            } else {
                signUpBtn.setEnabled(false);
            }
        });
    }

    public void registerUser(View view) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        registerActivityViewModel.register(username.getEditText().getText().toString(),
                    pwd.getEditText().getText().toString(), pwd2.getEditText().getText().toString(),
                    email.getEditText().getText().toString(), date);
    }

    private boolean showRegBtn(){
        boolean isEmpty = username.getEditText().getText().toString().trim().length() == 0 ||
                pwd.getEditText().getText().toString().trim().length() == 0 ||
                pwd2.getEditText().getText().toString().trim().length() == 0 ||
                email.getEditText().getText().toString().trim().length() == 0;

        boolean error = username.getError() != null || pwd.getError() != null ||
                pwd2.getError() != null || pwd2.getError() != null || email.getError() != null;
        String a = username.getError() == null ? "null": "noNull";
        Log.d("what", "showbtn " + a);
        return !isEmpty && !error;
    }

    public void setErrors() {
        username.setError(null);
        pwd.setError(null);
        pwd2.setError(null);
        email.setError(null);
        birthday.setError(null);
    }
}