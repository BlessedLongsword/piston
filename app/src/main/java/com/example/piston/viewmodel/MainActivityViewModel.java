package com.example.piston.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.AuthRepository;

public class MainActivityViewModel extends ViewModel {

    public MutableLiveData<Boolean> isSignedIn = new MutableLiveData<>(true);
    private final AuthRepository authRepository;

    public MainActivityViewModel() {
        authRepository = new AuthRepository(isSignedIn);
    }

    public void logout() {
        authRepository.logout();
    }

}
