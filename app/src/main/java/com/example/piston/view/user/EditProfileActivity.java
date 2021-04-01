package com.example.piston.view.user;

import android.os.Bundle;
import android.view.View;

import com.example.piston.R;
import com.example.piston.view.PistonActivity;

public class EditProfileActivity extends PistonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    public void editProfile(View view) {
        onBackPressed();
    }
}
