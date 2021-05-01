package com.example.piston.authentication.googleRegister;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.authentication.register.RegisterResult;

import java.util.Objects;

public class GoogleRegisterViewModel extends ViewModel implements GoogleRegisterRepository.IGoogleRegister {

    private final MutableLiveData<String> username = new MutableLiveData<>("");
    private final MutableLiveData<String> birthDate = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> registerEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> checkEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> finishedRegister = new MutableLiveData<>(false);

    private final MutableLiveData<RegisterResult.UsernameError> usernameError = new MutableLiveData<>(RegisterResult.UsernameError.NONE);
    private final MutableLiveData<RegisterResult.BirthDateError> birthDateError = new MutableLiveData<>(RegisterResult.BirthDateError.NONE);

    private final GoogleRegisterRepository registerRepository = new GoogleRegisterRepository(this);
    private final String idToken;

    public GoogleRegisterViewModel(String idToken) {
        this.idToken = idToken;
    }

    public void register() {
        try {
            registerRepository.register(username.getValue(), birthDate.getValue(), idToken);
            loading.setValue(true);
        } catch (Exception e) {
            Log.w("DBWriteTAG", "Something went wrong went converting Date", e);
        }
    }

    public void usernameUpdate() {
        onFieldChanged();
        registerRepository.checkUsername(Objects.requireNonNull(username.getValue()));
    }
    public void birthdayUpdate() {
        onFieldChanged();
        registerRepository.checkBirthDate(birthDate.getValue());
    }

    public void onFieldChanged() {
        registerEnabled.setValue((Objects.requireNonNull(getUsername().getValue().trim()).length() > 0) &&
                (getBirthDate().getValue().trim().length() > 0) &&
                (getCheckEnabled().getValue()) &&
                (getUsernameError().getValue() == RegisterResult.UsernameError.NONE) &&
                (getBirthDateError().getValue() == RegisterResult.BirthDateError.NONE));
    }

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
