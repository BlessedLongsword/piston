package com.example.piston.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.viewmodel.PistonViewModel;

public class PistonFragment extends Fragment {

    public PistonViewModel pistonViewModel;

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        initView();
    }

    private void initView() {
        pistonViewModel = new ViewModelProvider(this).get(PistonViewModel.class);
    }

}
