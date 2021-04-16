package com.example.piston.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.MainRepository;

public class MainActivityViewModel extends ViewModel implements MainRepository.IMain {

    public MutableLiveData<Boolean> isSignedIn = new MutableLiveData<>(true);
    private final MainRepository mainRepository = new MainRepository(this);

    public void logout() {
        mainRepository.logout();
    }

    @Override
    public void setSignedIn(boolean signedIn) {
        isSignedIn.setValue(signedIn);
    }
}
