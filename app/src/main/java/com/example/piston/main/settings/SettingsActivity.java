package com.example.piston.main.settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

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

        subscribe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TODO
                    Log.d("what","checked");
                }
                else {
                    Log.d("what","unchecked");
                }
            }
        });

        dark_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TODO
                }
                else {
                    //TODO
                }
            }
        });

        show_nsfw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TODO
                }
                else {
                    //TODO
                }
            }
        });

        blur_nsfw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //TODO
                }
                else {
                    //TODO
                }
            }
        });
    }

    public void saveSettings(View view) {
        finish();
    }
}