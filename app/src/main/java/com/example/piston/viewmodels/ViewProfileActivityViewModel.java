package com.example.piston.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.ProfileResult;
import com.example.piston.data.repositories.ProfileRepository;

public class ViewProfileActivityViewModel extends ViewModel implements ProfileRepository.IProfile{

    private final MutableLiveData<String> username = new MutableLiveData<>("");
    private final MutableLiveData<String> fullName = new MutableLiveData<>("");
    private final MutableLiveData<String> phone = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<String> birthDate = new MutableLiveData<>("");

    private final MutableLiveData<ProfileResult.BirthDateError> birthDateError = new MutableLiveData<>(ProfileResult.BirthDateError.NONE);

    private final ProfileRepository profileRepository = new ProfileRepository(this);

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<String> getFullName() {
        return fullName;
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getBirthDate() {
        return birthDate;
    }

    public MutableLiveData<ProfileResult.BirthDateError> getBirthDateError() {
        return birthDateError;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getFinishedRegister() {
        return finishedRegister;
    }

}
