package com.example.piston.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.piston.R;

public class LoginActivity extends PistonActivity {

    private EditText username, pwd;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.username = findViewById(R.id.user_editText);
        this.pwd = findViewById(R.id.pass_editText);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setEnabled(false);

        pistonViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult.getError().equals(R.string.wrong_user))
                username.setError(getString(R.string.wrong_user));
            else if (loginResult.getError().equals(R.string.wrong_password))
                pwd.setError(getString(R.string.wrong_password));
            else
                startActivity(new Intent(this, MainActivity.class));
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
                boolean enabled = username.length()>0 && pwd.length()>0;
                signInButton.setEnabled(enabled);
            }
        };
        username.addTextChangedListener(afterTextChangedListener);
        pwd.addTextChangedListener(afterTextChangedListener);
    }

    public void login(View view) {
        pistonViewModel.login(username.getText().toString(), pwd.getText().toString());
    }


    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }



}
