package com.example.piston.view.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.piston.R;
import com.example.piston.view.PistonActivity;
import com.example.piston.view.user.EditProfileActivity;

public class ViewProfileActivity extends PistonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
    }

    public void editProfile(View view) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}