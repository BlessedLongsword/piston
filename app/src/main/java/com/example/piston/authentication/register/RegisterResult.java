package com.example.piston.authentication.register;

import androidx.databinding.BaseObservable;

public class RegisterResult extends BaseObservable {

    public enum UsernameError {NONE, EMPTY, EXISTS }

    public enum PasswordError {NONE, EMPTY, INVALID }

    public enum ConfirmPasswordError {NONE, INVALID }

    public enum EmailError {NONE, EMPTY, INVALID, EXISTS, UNEXPECTED }

    public enum BirthDateError {NONE, INVALID }

    public enum CheckError {NONE, EMPTY }

}
