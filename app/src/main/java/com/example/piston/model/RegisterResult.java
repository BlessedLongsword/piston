package com.example.piston.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class RegisterResult extends BaseObservable {

    public enum UsernameError { NONE, EMPTY, EXISTS }
    public enum PasswordError { NONE, EMPTY, INVALID }
    public enum ConfirmPasswordError { NONE, INVALID }
    public enum EmailError { NONE, EMPTY, INVALID, EXISTS }
    public enum BirthdayError { NONE, INVALID }
    public enum CheckError { NONE, EMPTY }

    private UsernameError usernameError = UsernameError.NONE;
    private PasswordError passwordError = PasswordError.NONE;
    private ConfirmPasswordError confirmPasswordError = ConfirmPasswordError.NONE;
    private EmailError emailError = EmailError.NONE;
    private BirthdayError birthdayError = BirthdayError.NONE;
    private CheckError checkError = CheckError.NONE;

    private boolean error;

    @Bindable
    public UsernameError getUsernameError() {
        return usernameError;
    }

    public void setUsernameError(UsernameError usernameError) {
        this.usernameError = usernameError;
    }

    @Bindable
    public PasswordError getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(PasswordError passwordError) {
        this.passwordError = passwordError;
    }

    @Bindable
    public ConfirmPasswordError getConfirmPasswordError() {
        return confirmPasswordError; }

    public void setConfirmPasswordError(ConfirmPasswordError confirmPasswordError) {
        this.confirmPasswordError = confirmPasswordError;
    }

    @Bindable
    public EmailError getEmailError() {
        return emailError; }

    public void setEmailError(EmailError emailError) {
        this.emailError = emailError;
    }

    @Bindable
    public BirthdayError getBirthdayError() {
        return birthdayError;
    }

    public void setBirthdayError(BirthdayError birthdayError) {
        this.birthdayError = birthdayError;
    }

    @Bindable
    public CheckError getCheckError() {
        return checkError;
    }

    public void setCheckError(CheckError checkError) {
        this.checkError = checkError;
    }

}

/*import androidx.annotation.Nullable;

public class RegisterResult {
    @Nullable
    private Integer error;
    @Nullable
    private boolean sucess;

    RegisterResult(@Nullable Integer error) {
        this.error = error;
    }

    RegisterResult(@Nullable boolean sucess){ this.sucess = sucess; }

    @Nullable
    public Integer getError() {
        return error;
    }

    @Nullable
    public boolean getSuccess() { return sucess; }
}*/
