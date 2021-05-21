package com.example.piston.launch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.main.MainActivity;
import com.example.piston.authentication.login.LoginActivity;
import com.example.piston.utilities.NotificationsService;
import com.example.piston.utilities.Values;

import java.util.List;

public class LaunchActivity extends AppCompatActivity {

    private LaunchViewModel viewModel;
    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(Values.SHARED_PREFS, Context.MODE_PRIVATE);
        boolean followSystem = false;

        if (Build.VERSION.SDK_INT >= 29) {
            followSystem = prefs.getBoolean(Values.THEME_FOLLOW_SYSTEM, false);
            if (followSystem)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        if (!followSystem) {
            boolean manualDarkModeEnabled = prefs.getBoolean(Values.DARK_THEME, false);
            if (manualDarkModeEnabled)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        viewModel = new ViewModelProvider(this).get(LaunchViewModel.class);
        checkIfUserIsAuthenticated();

        uri = getIntent().getData();
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
        putUriInfo(intent);
        startActivity(intent);
        finish();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        putUriInfo(intent);
        startActivity(intent);
        finish();
    }

    private void putUriInfo(Intent intent) {
        if (uri != null) {
            List<String> parameters = uri.getPathSegments();
            intent.putExtra(Values.SCOPE, parameters.get(0));
            intent.putExtra(Values.SECTION_ID, parameters.get(1));
            try {
                intent.putExtra(Values.POST_ID, parameters.get(2));
            } catch (Exception ignored) {

            }
            intent.putExtra(Values.FROM_SHARE, true);
        }
    }
}
