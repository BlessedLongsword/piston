package com.example.piston.authentication.register;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class RegisterActivityViewModel extends ViewModel implements RegisterRepository.IRegister {

    private final MutableLiveData<String> username = new MutableLiveData<>("");
    private final MutableLiveData<String> email = new MutableLiveData<>("");
    private final MutableLiveData<String> password = new MutableLiveData<>("");
    private final MutableLiveData<String> confirmPassword = new MutableLiveData<>("");
    private final MutableLiveData<String> birthDate = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> registerEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> checkEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> finishedRegister = new MutableLiveData<>(false);

    private final MutableLiveData<RegisterResult.UsernameError> usernameError = new MutableLiveData<>(RegisterResult.UsernameError.NONE);
    private final MutableLiveData<RegisterResult.PasswordError> passwordError = new MutableLiveData<>(RegisterResult.PasswordError.NONE);
    private final MutableLiveData<RegisterResult.ConfirmPasswordError> confirmPasswordError = new MutableLiveData<>(RegisterResult.ConfirmPasswordError.NONE);
    private final MutableLiveData<RegisterResult.EmailError> emailError = new MutableLiveData<>(RegisterResult.EmailError.NONE);
    private final MutableLiveData<RegisterResult.BirthDateError> birthDateError = new MutableLiveData<>(RegisterResult.BirthDateError.NONE);
    private final MutableLiveData<RegisterResult.CheckError> checkError = new MutableLiveData<>(RegisterResult.CheckError.NONE);


    private final RegisterRepository registerRepository = new RegisterRepository(this);

    public void register() {
        try {
            registerRepository.register(username.getValue(), email.getValue(),
                    password.getValue(), birthDate.getValue());
            loading.setValue(true);
        } catch (Exception e) {
            Log.w("DBWriteTAG", "Something went wrong went converting Date", e);
        }
    }

    public void usernameUpdate() {
        onFieldChanged();
        registerRepository.checkUsername(username.getValue());
    }
    public void passwordUpdate() {
        onFieldChanged();
        registerRepository.checkPassword(password.getValue());
    }
    public void confirmPasswordUpdate() {
        onFieldChanged();
        registerRepository.checkConfirmPassword(password.getValue(), confirmPassword.getValue());
    }
    public void birthdayUpdate() {
        onFieldChanged();
        registerRepository.checkBirthDate(birthDate.getValue());
    }
    public void emailUpdate() {
        onFieldChanged();
        registerRepository.checkEmail(email.getValue());
    }

    public void onFieldChanged() {
        registerEnabled.setValue((Objects.requireNonNull(getUsername().getValue().trim()).length() > 0) &&
                (Objects.requireNonNull(getPassword().getValue().trim()).length() > 0) &&
                (getConfirmPassword().getValue().trim().length() > 0) &&
                (getEmail().getValue().trim().length() > 0) &&
                (getBirthDate().getValue().trim().length() > 0) &&
                (getCheckEnabled().getValue()) &&
                (getUsernameError().getValue() == RegisterResult.UsernameError.NONE) &&
                (getEmailError().getValue() == RegisterResult.EmailError.NONE) &&
                (getPasswordError().getValue() == RegisterResult.PasswordError.NONE) &&
                (getConfirmPasswordError().getValue() == RegisterResult.ConfirmPasswordError.NONE) &&
                (getBirthDateError().getValue() == RegisterResult.BirthDateError.NONE));
    }

    @Override
    public void setUsernameErrorStatus(RegisterResult.UsernameError usernameError) {
        this.usernameError.setValue(usernameError);
    }

    @Override
    public void setEmailErrorStatus(RegisterResult.EmailError emailError) {
        this.emailError.setValue(emailError);
    }

    @Override
    public void setPasswordStatus(RegisterResult.PasswordError passwordError) {
        this.passwordError.setValue(passwordError);
    }

    @Override
    public void setConfirmPasswordStatus(RegisterResult.ConfirmPasswordError confirmPasswordError) {
        this.confirmPasswordError.setValue(confirmPasswordError);
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

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<String> getConfirmPassword() {
        return confirmPassword;
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

    public LiveData<RegisterResult.PasswordError> getPasswordError() {
        return passwordError;
    }

    public LiveData<RegisterResult.ConfirmPasswordError> getConfirmPasswordError() {
        return confirmPasswordError;
    }

    public LiveData<RegisterResult.EmailError> getEmailError() {
        return emailError;
    }

    public LiveData<RegisterResult.BirthDateError> getBirthDateError() {
        return birthDateError;
    }

    public LiveData<RegisterResult.CheckError> getCheckError() {
        return checkError;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getFinishedRegister() {
        return finishedRegister;
    }

}
