package com.example.piston.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.viewmodel.PistonViewModel;

public class PistonActivity extends AppCompatActivity {

    PistonViewModel pistonViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        initView();
    }
    
    private void initView() {
        pistonViewModel = new ViewModelProvider(this).get(PistonViewModel.class);
    }
    
}
