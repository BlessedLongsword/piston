package com.example.piston.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends PistonActivity {

    EditText username;
    EditText email;
    EditText pwd;
    EditText pwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.username = findViewById(R.id.username);
        this.email = findViewById(R.id.email);
        this.pwd = findViewById(R.id.pwd);
        this.pwd2 = findViewById(R.id.pwd2);
    }

    public void register(View view) {
        try {
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            pistonViewModel.registerUser(username.getText().toString(), pwd.getText().toString(), pwd2.getText().toString(), email.getText().toString(),date );
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            onBackPressed();
        }catch (Exception e){
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }
}