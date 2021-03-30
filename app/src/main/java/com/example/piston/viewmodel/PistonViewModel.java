package com.example.piston.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.PistonModel;

public class PistonViewModel extends ViewModel {

    private MutableLiveData<PistonModel> modelMutableLiveData;

    public PistonViewModel() {
        modelMutableLiveData = new MutableLiveData<>();
    }

}
