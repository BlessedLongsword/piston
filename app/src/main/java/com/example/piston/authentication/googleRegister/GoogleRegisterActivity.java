package com.example.piston.authentication.googleRegister;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityGoogleRegisterBinding;
import com.example.piston.utilities.MyViewModelFactory;

public class GoogleRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        String idToken = getIntent().getStringExtra("idToken");

        GoogleRegisterViewModel viewModel = new ViewModelProvider(this, new MyViewModelFactory(idToken))
                .get(GoogleRegisterViewModel.class);

        ActivityGoogleRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_google_register);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.getFinishedRegister().observe(this, aBoolean -> {
            if (aBoolean) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}