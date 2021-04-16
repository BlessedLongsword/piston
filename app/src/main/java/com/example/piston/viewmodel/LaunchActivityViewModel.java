package com.example.piston.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.LaunchRepository;

public class LaunchActivityViewModel extends ViewModel implements LaunchRepository.ILaunch {

    public MutableLiveData<Boolean> isSignedIn = new MutableLiveData<>();
    private final LaunchRepository launchRepository = new LaunchRepository(this);

    public void checkIfUserIsAuthenticated() {
        launchRepository.checkIfUserIsAuthenticated();
    }

    @Override
    public void setIsSignedId(boolean isSignedIn) {
        this.isSignedIn.setValue(isSignedIn);
    }
}
