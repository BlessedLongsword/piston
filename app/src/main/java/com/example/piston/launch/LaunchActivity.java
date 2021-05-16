package com.example.piston.launch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.main.MainActivity;
import com.example.piston.authentication.login.LoginActivity;
import com.example.piston.main.posts.PostActivity;
import com.example.piston.utilities.Values;

import java.util.List;

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

        Uri uri = getIntent().getData();

        if (uri != null) {
            List<String> parameters = uri.getPathSegments();
            Intent intent = new Intent(this, PostActivity.class);
            intent.putExtra(Values.SCOPE, parameters.get(parameters.size() - 3));
            intent.putExtra(Values.SECTION_ID, parameters.get(parameters.size() - 2));
            intent.putExtra(Values.POST_ID, parameters.get(parameters.size() - 1));
        }
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
