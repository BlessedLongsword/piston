package com.example.piston.data;

import androidx.databinding.BaseObservable;

public class RegisterResult extends BaseObservable {

    public enum UsernameError {NONE, EMPTY, EXISTS }

    public enum PasswordError {NONE, EMPTY, INVALID }

    public enum ConfirmPasswordError {NONE, INVALID }

    public enum EmailError {NONE, EMPTY, INVALID, EXISTS, UNEXPECTED }

    public enum BirthdayError {NONE, INVALID }

    public enum CheckError {NONE, EMPTY }

}
