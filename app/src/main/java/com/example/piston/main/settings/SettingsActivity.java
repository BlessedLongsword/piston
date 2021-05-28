package com.example.piston.main.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;

import com.example.piston.R;
import com.example.piston.databinding.ActivitySettingsBinding;
import com.example.piston.main.notifications.NotificationsService;
import com.example.piston.utilities.Values;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(Values.SHARED_PREFS, Context.MODE_PRIVATE);

        boolean notificationsEnd = getIntent().getBooleanExtra(Values.NOTIFICATIONS_END, false);
        if (notificationsEnd) {
            stopService(new Intent(this, NotificationsService.class));
            prefs.edit().putBoolean(Values.NOTIFICATIONS_ENABLED, false).apply();
            finish();
        }

        ActivitySettingsBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_settings);

        boolean notificationsEnabled = prefs.getBoolean(Values.NOTIFICATIONS_ENABLED, false);
        binding.notificationsEnabled.setChecked(notificationsEnabled);

        binding.notificationsEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Intent intent = new Intent(this, NotificationsService.class);
            if (!isChecked)
                stopService(intent);
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    startForegroundService(intent);
                else
                    startService(intent);
            }
            prefs.edit().putBoolean(Values.NOTIFICATIONS_ENABLED, isChecked).apply();
        });

        boolean followSystem = false;
        if (Build.VERSION.SDK_INT >= 29) {
            followSystem = prefs.getBoolean(Values.THEME_FOLLOW_SYSTEM, false);
            Objects.requireNonNull(binding.settingsFollowSystem).setChecked(followSystem);
            binding.settingsDarkMode.setEnabled(!followSystem);

            binding.settingsFollowSystem.setOnCheckedChangeListener((buttonView, isChecked) -> {
                prefs.edit().putBoolean(Values.THEME_FOLLOW_SYSTEM, isChecked).apply();
                binding.settingsDarkMode.setEnabled(!isChecked);
                if (isChecked)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                else
                    AppCompatDelegate.setDefaultNightMode((binding.settingsDarkMode.isChecked()?
                            AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO));
            });
        }
        if (!followSystem) {
            boolean darkMode = prefs.getBoolean(Values.DARK_THEME, false);
            binding.settingsDarkMode.setChecked(darkMode);
        }

        binding.settingsDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(Values.DARK_THEME, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode((binding.settingsDarkMode.isChecked()?
                    AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO));
        });

        boolean nsfw = prefs.getBoolean(Values.NSFW_ENABLED, false);
        binding.settingsShowNsfw.setChecked(nsfw);

        binding.settingsShowNsfw.setOnCheckedChangeListener((buttonView, isChecked) ->
            prefs.edit().putBoolean(Values.NSFW_ENABLED, isChecked).apply()
        );

        binding.settingsTopAppBar.setNavigationOnClickListener((view) -> finish());
    }
}