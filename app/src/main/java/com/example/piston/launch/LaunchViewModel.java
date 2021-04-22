package com.example.piston.launch;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LaunchViewModel extends ViewModel implements LaunchRepository.ILaunch {

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
