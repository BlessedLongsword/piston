package com.example.piston.view.others;

import android.os.Bundle;
import android.view.View;

import com.example.piston.R;
import com.example.piston.view.PistonActivity;

public class SettingsActivity extends PistonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void saveSettings(View view) {
        onBackPressed();
    }
}