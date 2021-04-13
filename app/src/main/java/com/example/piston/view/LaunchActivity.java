package com.example.piston.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.view.sections.MainActivity;
import com.example.piston.view.user.LoginActivity;
import com.example.piston.viewmodel.LaunchActivityViewModel;

public class LaunchActivity extends AppCompatActivity {

    private LaunchActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LaunchActivityViewModel.class);
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
