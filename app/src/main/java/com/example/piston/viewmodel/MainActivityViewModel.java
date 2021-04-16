package com.example.piston.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.LoginRepository;

public class MainActivityViewModel extends ViewModel {

    public MutableLiveData<Boolean> isSignedIn = new MutableLiveData<>(true);
   // private final LoginRepository loginRepository;

    public MainActivityViewModel() {
        //loginRepository = new LoginRepository(this);
    }

    public void logout() {
        //loginRepository.logout();
    }

}
