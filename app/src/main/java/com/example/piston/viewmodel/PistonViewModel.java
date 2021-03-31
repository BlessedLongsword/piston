package com.example.piston.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.PistonModel;
import com.example.piston.model.PostStorage;

public class PistonViewModel extends ViewModel {

    private MutableLiveData<PostStorage> postStorageMLD;

    public PistonViewModel() {
        postStorageMLD = new MutableLiveData<>();
    }

    public LiveData<PostStorage> getPostStorageMLD() {
        return postStorageMLD;
    }

}
