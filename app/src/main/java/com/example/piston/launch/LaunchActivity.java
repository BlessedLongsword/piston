package com.example.piston.launch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.main.MainActivity;
import com.example.piston.authentication.login.LoginActivity;

public class LaunchActivity extends AppCompatActivity {

    private LaunchViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.piston", Context.MODE_PRIVATE);

        String darkModeKey = "com.example.piston.darkMode";
        boolean manualDarkModeEnabled = prefs.getBoolean(darkModeKey, false);
        if (manualDarkModeEnabled)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        viewModel = new ViewModelProvider(this).get(LaunchViewModel.class);
        checkIfUserIsAuthenticated();
    }

    private void checkIfUserIsAuthenticated() {
        viewModel.isSignedIn.observe(this, isSignedIn -> {
            if (isSignedIn)
                goToMainActivity();
            else
                goToLoginActivity();
        });
        viewModel.checkIfUserIsAuthenticated();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
