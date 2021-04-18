package com.example.piston.viewmodels;

import androidx.annotation.Nullable;

/**
 * Data validation state of the register form.
 */

public class RegisterFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer password2Error;
    @Nullable
    private Integer isEmailValid;
    @Nullable
    private Integer emptyUsername;
    @Nullable
    private Integer emptyPwd;
    @Nullable
    private Integer emptyEmail;
    @Nullable
    private Integer isBdayValid;

    private boolean isDataValid;

    RegisterFormState(@Nullable Integer usernameError, @Nullable Integer passwordError,
                                  @Nullable Integer password2Error, @Nullable Integer isEmailValid,
                                  @Nullable Integer emptyUsername, @Nullable Integer emptyPwd,
                                  @Nullable Integer emptyEmail, @Nullable Integer isBdayValid) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.password2Error = password2Error;
        this.isEmailValid = isEmailValid;
        this.emptyEmail = emptyEmail;
        this.emptyUsername = emptyUsername;
        this.emptyPwd = emptyPwd;
        this.isBdayValid = isBdayValid;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.password2Error = null;
        this.isEmailValid = null;
        this.emptyPwd = null;
        this.emptyUsername = null;
        this.emptyUsername = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getPassword2Error() { return password2Error;}

    @Nullable
    public Integer getEmailError() { return isEmailValid; }

    @Nullable
    public Integer getEmptyUsernameError() { return emptyUsername; }

    @Nullable
    public Integer getEmptyPwdError() { return emptyPwd; }

    @Nullable
    public Integer getEmailExistError() { return emptyEmail; }

    @Nullable
    public Integer getIsBdayValidError() { return isBdayValid; }

    public boolean isDataValid() {
        return isDataValid;
    }
}
