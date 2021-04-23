package com.example.piston.main.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.piston.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {

    SwitchMaterial subscribe;
    SwitchMaterial dark_mode;
    SwitchMaterial show_nsfw;
    SwitchMaterial blur_nsfw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        subscribe = findViewById(R.id.settings_subscribe);
        dark_mode = findViewById(R.id.settings_darkMode);
        show_nsfw = findViewById(R.id.settings_show_nsfw);
        blur_nsfw = findViewById(R.id.settings_blur);


        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.piston", Context.MODE_PRIVATE);

        String darkModeKey = "com.example.piston.darkMode";
        boolean manualDarkModeEnabled = prefs.getBoolean(darkModeKey, false);

        dark_mode.setChecked(manualDarkModeEnabled);

        if (manualDarkModeEnabled)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        dark_mode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                prefs.edit().putBoolean(darkModeKey, true).apply();
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                prefs.edit().putBoolean(darkModeKey, false).apply();
            }
        });

        subscribe.setOnCheckedChangeListener((buttonView, isChecked) -> {
        });

        show_nsfw.setOnCheckedChangeListener((buttonView, isChecked) -> {
        });

        blur_nsfw.setOnCheckedChangeListener((buttonView, isChecked) -> {
        });
    }
}