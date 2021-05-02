package com.example.piston.authentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.authentication.register.RegisterResult;

public class CommonRegisterViewModel extends ViewModel implements CommonRegisterRepository.ICommonRegister {

    protected final MutableLiveData<String> username = new MutableLiveData<>("");
    protected final MutableLiveData<String> birthDate = new MutableLiveData<>("");
    protected final MutableLiveData<Boolean> registerEnabled = new MutableLiveData<>(false);
    protected final MutableLiveData<Boolean> checkEnabled = new MutableLiveData<>(false);
    protected final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    protected final MutableLiveData<Boolean> finishedRegister = new MutableLiveData<>(false);

    protected final MutableLiveData<RegisterResult.UsernameError> usernameError = new MutableLiveData<>(RegisterResult.UsernameError.NONE);
    protected final MutableLiveData<RegisterResult.BirthDateError> birthDateError = new MutableLiveData<>(RegisterResult.BirthDateError.NONE);

    @Override
    public void setUsernameErrorStatus(RegisterResult.UsernameError usernameError) {
        this.usernameError.setValue(usernameError);
    }

    @Override
    public void setBirthDateStatus(RegisterResult.BirthDateError birthDateError) {
        this.birthDateError.setValue(birthDateError);
    }

    @Override
    public void setLoadingFinished() {
        loading.setValue(false);
    }

    @Override
    public void setRegisterFinished() {
        finishedRegister.setValue(true);
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<String> getBirthDate() {
        return birthDate;
    }

    public MutableLiveData<Boolean> getCheckEnabled() {
        return checkEnabled;
    }

    public LiveData<Boolean> getRegisterEnabled() {
        return registerEnabled;
    }

    public LiveData<RegisterResult.UsernameError> getUsernameError() {
        return usernameError;
    }

    public LiveData<RegisterResult.BirthDateError> getBirthDateError() {
        return birthDateError;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getFinishedRegister() {
        return finishedRegister;
    }
}
