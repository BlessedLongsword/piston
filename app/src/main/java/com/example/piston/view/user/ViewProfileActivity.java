package com.example.piston.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piston.R;
import com.example.piston.model.User;
import com.example.piston.view.PistonActivity;
import com.example.piston.view.user.EditProfileActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

public class ViewProfileActivity extends PistonActivity {

    TextInputLayout username;
    TextInputLayout fullName;
    TextInputLayout phoneNumber;
    TextInputLayout email;
    TextInputLayout bday;
    TextView featuredPost;
    Button cancel;
    Button edit;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        username = findViewById(R.id.viewProfile_username);
        fullName = findViewById(R.id.viewProfile_fullName);
        phoneNumber = findViewById(R.id.viewProfile_phone);
        email = findViewById(R.id.viewProfile_email);
        bday = findViewById(R.id.viewProfile_date);
        featuredPost = findViewById(R.id.viewProfile_featPost);
        cancel = findViewById(R.id.viewProfile_cancel);
        edit = findViewById(R.id.viewProfile_edit);
        save = findViewById(R.id.viewProfile_save);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText().toString().equals(getString(R.string.view_profile_edit))) {
                    edit();
                }
                else {
                    cancel();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        try {
            String userFile = "userFile";
            FileInputStream fin = openFileInput(userFile);
            ObjectInputStream ois = new ObjectInputStream(fin);
            User user = (User) ois.readObject();
            ois.close();
            fin.close();
            username.getEditText().setText(user.getUsername());
            email.getEditText().setText(user.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void cancel() {
        save.setEnabled(true);
        save.setVisibility(View.INVISIBLE);
        edit.setEnabled(true);
        edit.setVisibility(View.VISIBLE);
        fullName.getEditText().clearFocus();
        fullName.getEditText().setFocusableInTouchMode(false);
        phoneNumber.getEditText().clearFocus();
        phoneNumber.getEditText().setFocusableInTouchMode(false);
        bday.getEditText().clearFocus();
        bday.getEditText().setFocusableInTouchMode(false);
        cancel.setVisibility(View.INVISIBLE);
        cancel.setEnabled(false);
    }

    private void edit(){
        save.setEnabled(true);
        save.setVisibility(View.VISIBLE);
        edit.setEnabled(false);
        edit.setVisibility(View.INVISIBLE);
        fullName.getEditText().setFocusableInTouchMode(true);
        phoneNumber.getEditText().setFocusableInTouchMode(true);
        bday.getEditText().setFocusableInTouchMode(true);
        cancel.setVisibility(View.VISIBLE);
        cancel.setEnabled(true);
    }
}