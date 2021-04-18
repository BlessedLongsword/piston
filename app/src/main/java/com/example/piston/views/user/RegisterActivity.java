package com.example.piston.views.user;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityRegisterBinding;
import com.example.piston.viewmodels.RegisterActivityViewModel;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RegisterActivityViewModel registerActivityViewModel = new ViewModelProvider(this).get(RegisterActivityViewModel.class);

        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setViewModel(registerActivityViewModel);
        binding.setLifecycleOwner(this);
        registerActivityViewModel.getFinishedRegister().observe(this, aBoolean -> {
            if (aBoolean)
                finish();
        });
    }
}