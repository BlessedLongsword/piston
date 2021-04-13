package com.example.piston.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.AuthRepository;

public class LaunchActivityViewModel extends ViewModel {

    public MutableLiveData<Boolean> isSignedIn = new MutableLiveData<>();
    private final AuthRepository authRepository;

    public LaunchActivityViewModel() {
        authRepository = new AuthRepository(isSignedIn);
    }

    public void checkIfUserIsAuthenticated() {
        authRepository.checkIfUserIsAuthenticatedInFirebase();
    }
}
