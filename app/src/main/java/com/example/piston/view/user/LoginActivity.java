package com.example.piston.view.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.model.User;
import com.example.piston.view.MainActivity;
import com.example.piston.viewmodel.LoginActivityViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel loginActivityViewModel;

    private TextInputLayout username, pwd;
    private final String userCachePath = "logged_user";
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivityViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        this.username = findViewById(R.id.user_textField);
        this.pwd = findViewById(R.id.pass_textField);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setEnabled(false);

        loginActivityViewModel.getLoginResult().observe(this, loginResult -> {
            username.setError(null);
            pwd.setError(null);
            if (loginResult == null) {
                return;
            }
            if (loginResult.getError() != null) {
                if (loginResult.getError() == R.string.wrong_user) {
                    username.setError(getString(R.string.wrong_user));
                }
                else if (loginResult.getError()== R.string.wrong_password)
                    pwd.setError(getString(R.string.wrong_password));
            }
            if (loginResult.getSuccess() != null) {
                setResult(Activity.RESULT_OK);
                startActivity(new Intent(this, MainActivity.class));
                try {
                    File cacheFile = new File(getApplicationContext().getCacheDir(), userCachePath);
                    String userFile = "userFile";
                    FileOutputStream fOut = openFileOutput(userFile, Context.MODE_PRIVATE);
                    User user = new User("admin", "admin@gmail.com",
                            new Date(System.currentTimeMillis()), "admin");
                    ObjectOutputStream oos = new ObjectOutputStream(fOut);
                    oos.writeObject(user);
                    oos.close();
                    fOut.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean enabled = username.getEditText().length()>0 && pwd.getEditText().length()>0;
                signInButton.setEnabled(enabled);
            }
        };
        username.getEditText().addTextChangedListener(afterTextChangedListener);
        pwd.getEditText().addTextChangedListener(afterTextChangedListener);
    }

    public void login(View view) {
        loginActivityViewModel.login(username.getEditText().getText().toString(), pwd.getEditText().getText().toString());
    }


    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
