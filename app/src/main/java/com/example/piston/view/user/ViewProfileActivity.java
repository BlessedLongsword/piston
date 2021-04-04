package com.example.piston.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;
import com.example.piston.model.User;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class ViewProfileActivity extends AppCompatActivity {

    TextInputLayout username, fullName, phoneNumber, email, bday;
    TextView featuredPost;
    Button cancel, edit, save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    public void cancel (View v) {
        save.setEnabled(false);
        save.setVisibility(View.INVISIBLE);
        edit.setEnabled(true);
        edit.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        cancel.setEnabled(false);

        fullName.getEditText().setInputType(InputType.TYPE_NULL);
        phoneNumber.getEditText().setInputType(InputType.TYPE_NULL);
        bday.getEditText().setInputType(InputType.TYPE_NULL);

        fullName.getEditText().setFocusableInTouchMode(false);
        phoneNumber.getEditText().setFocusableInTouchMode(false);
        bday.getEditText().setFocusableInTouchMode(false);

        fullName.getEditText().clearFocus();
        phoneNumber.getEditText().clearFocus();
        bday.getEditText().clearFocus();
    }

    public void edit (View v) {
        save.setEnabled(true);
        save.setVisibility(View.VISIBLE);
        edit.setEnabled(false);
        edit.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.VISIBLE);
        cancel.setEnabled(true);


        fullName.getEditText().setFocusableInTouchMode(true);
        phoneNumber.getEditText().setFocusableInTouchMode(true);
        bday.getEditText().setFocusableInTouchMode(true);

        fullName.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        phoneNumber.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        bday.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

        fullName.getEditText().requestFocus();
    }

    private void init () {
        username = findViewById(R.id.viewProfile_username);
        fullName = findViewById(R.id.viewProfile_fullName);
        phoneNumber = findViewById(R.id.viewProfile_phone);
        email = findViewById(R.id.viewProfile_email);
        bday = findViewById(R.id.viewProfile_date);
        featuredPost = findViewById(R.id.viewProfile_featPost);
        cancel = findViewById(R.id.viewProfile_cancel);
        edit = findViewById(R.id.viewProfile_edit);
        save = findViewById(R.id.viewProfile_save);

        username.getEditText().setInputType(InputType.TYPE_NULL);
        fullName.getEditText().setInputType(InputType.TYPE_NULL);
        phoneNumber.getEditText().setInputType(InputType.TYPE_NULL);
        email.getEditText().setInputType(InputType.TYPE_NULL);
        bday.getEditText().setInputType(InputType.TYPE_NULL);

        /*try {
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
        }*/
    }
}