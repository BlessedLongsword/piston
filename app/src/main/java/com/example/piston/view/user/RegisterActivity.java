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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.piston.R;
import com.example.piston.view.PistonActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends PistonActivity {

    EditText username;
    EditText email;
    EditText pwd;
    EditText pwd2;
    CheckBox tos;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.username = findViewById(R.id.username);
        this.email = findViewById(R.id.email);
        this.pwd = findViewById(R.id.pwd);
        this.pwd2 = findViewById(R.id.pwd2);
        this.tos = findViewById(R.id.tosCheck);
        this.signUpBtn = findViewById(R.id.registerBtn);

        pistonViewModel.getRegisterFormState().observe(this, registerFormState -> {
            if (registerFormState == null){
                return;
            }
            if (registerFormState.getEmptyUsernameError() != null) {
                username.setError(getString(registerFormState.getEmptyUsernameError()));
            }
            else if (registerFormState.getUsernameError() != null) {
                username.setError(getString(registerFormState.getUsernameError()));
            }
            else{
                username.setError(null);
            }
            if (registerFormState.getEmailError() != null) {
                email.setError(getString(registerFormState.getEmailError()));
            }
            else if (registerFormState.getEmailExistError() != null) {
                email.setError(getString(registerFormState.getEmailExistError()));
            }
            else {
                email.setError(null);
            }
            if (registerFormState.getEmptyPwdError() != null) {
                pwd.setError(getString(registerFormState.getEmptyPwdError()));
            }
            else if (registerFormState.getPasswordError() != null) {
                pwd.setError(getString(registerFormState.getPasswordError()));
            }
            else {
                pwd.setError(null);
            }
            if (registerFormState.getPassword2Error() != null) {
                pwd2.setError(getString(registerFormState.getPassword2Error()));
            }
            else {
                pwd2.setError(null);
            }
        });

        pistonViewModel.getRegisterResult().observe(this, registerResult -> {
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

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pistonViewModel.registerUsernameChanged(username.getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });
        pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pistonViewModel.registerPwd1Changed(pwd.getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });
        pwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pistonViewModel.registerPwd2Changed(pwd.getText().toString(), pwd2.getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pistonViewModel.registerEmailChanged(email.getText().toString());
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
            pistonViewModel.register(username.getText().toString(), pwd.getText().toString(),
                    pwd2.getText().toString(), email.getText().toString(), date);
    }

    private boolean showRegBtn(){
        boolean isEmpty = username.getText().toString().trim().length() == 0 ||
                pwd.getText().toString().trim().length() == 0 ||
                pwd2.getText().toString().trim().length() == 0 ||
                email.getText().toString().trim().length() == 0;

        boolean error = username.getError() != null || pwd.getError() != null ||
                pwd2.getError() != null || pwd2.getError() != null || email.getError() != null;
        String a = username.getError() == null ? "null": "noNull";
        Log.d("what", "showbtn " + a);
        return !isEmpty && !error;
    }
}