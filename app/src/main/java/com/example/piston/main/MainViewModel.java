package com.example.piston.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel implements MainRepository.IMain {

    public final MutableLiveData<Boolean> isSignedIn = new MutableLiveData<>(true);
    private final MainRepository mainRepository = new MainRepository(this);

    public void logout() {
        mainRepository.logout();
    }

    @Override
    public void setSignedIn(boolean signedIn) {
        isSignedIn.setValue(signedIn);
    }
}
