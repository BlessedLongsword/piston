package com.example.piston.view.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityRegisterBinding;
import com.example.piston.viewmodel.RegisterActivityViewModel;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RegisterActivityViewModel registerActivityViewModel = new ViewModelProvider(this).get(RegisterActivityViewModel.class);

        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setViewModel(registerActivityViewModel);
        binding.setLifecycleOwner(this);
    }

        /*
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
                finish();
            }
        });

        username.getEditText().addTextChangedListener(new CounterWatcher(getResources().getInteger(R.integer.username_max_length), username));
        username.getEditText().addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                username.setError(null);
                registerActivityViewModel.registerUsernameChanged(username.getEditText().getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });
        pwd.getEditText().addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                pwd.setError(null);
                registerActivityViewModel.registerPwd1Changed(pwd.getEditText().getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });
        pwd2.getEditText().addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                pwd2.setError(null);
                registerActivityViewModel.registerPwd2Changed(pwd.getEditText().getText().toString(),
                        pwd2.getEditText().getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });
        email.getEditText().addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                email.setError(null);
                registerActivityViewModel.registerEmailChanged(email.getEditText().getText().toString());
                signUpBtn.setEnabled(showRegBtn() && tos.isChecked());
            }
        });

        birthday.getEditText().addTextChangedListener(new BaseTextWatcher() {
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
        });*/

    /*public void registerUser(View view) {
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
    }*/
}