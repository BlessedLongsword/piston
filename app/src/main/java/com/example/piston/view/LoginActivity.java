package com.example.piston.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends PistonActivity {
    EditText username;
    EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.username = findViewById(R.id.user_editText);
        this.pwd = findViewById(R.id.pass_editText);
    }

    public void login(View view) {
        try {
            pistonViewModel.loginUser(username.getText().toString(), pwd.getText().toString());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }catch (Exception e){
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }



}
