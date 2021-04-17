package com.example.piston.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class LoginResult extends BaseObservable {

    public enum UsernameError { NONE, INVALID }
    public enum PasswordError { NONE, INCORRECT }

    private UsernameError usernameError = UsernameError.NONE;
    private PasswordError passwordError = PasswordError.NONE;

    private boolean signedIn = false;
    private boolean newUser = false;

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
    public boolean isSignedIn() {
        return signedIn;
    }

    public void setSignedIn(boolean signedIn) {
        this.signedIn = signedIn;
    }

    @Bindable
    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

}
