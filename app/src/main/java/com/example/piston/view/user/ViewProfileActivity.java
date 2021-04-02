package com.example.piston.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piston.R;
import com.example.piston.view.PistonActivity;
import com.example.piston.view.user.EditProfileActivity;

public class ViewProfileActivity extends PistonActivity {

    TextView username;
    TextView fullName;
    TextView phoneNumber;
    TextView email;
    TextView bday;
    TextView featuredPost;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        username = findViewById(R.id.viewProfile_username);
        fullName = findViewById(R.id.viewProfile_fullName);
        phoneNumber = findViewById(R.id.viewProfile_phone);
        email = findViewById(R.id.viewProfile_Email);
        bday = findViewById(R.id.viewProfile_date);
        featuredPost = findViewById(R.id.viewProfile_featPost);
        icon = findViewById(R.id.viewProfile_editBtn);

    }

    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}