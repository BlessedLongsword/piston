package com.example.piston.util;

import androidx.databinding.BindingAdapter;

import com.example.piston.R;
import com.example.piston.model.LoginResult;
import com.example.piston.model.RegisterResult;
import com.google.android.material.textfield.TextInputLayout;

public class BindingAdapters {

    @BindingAdapter("android:usernameError")
    public static void setUsernameErrorMessage(TextInputLayout view, LoginResult.UsernameError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case INVALID:
                view.setError(view.getContext().getString(R.string.wrong_user));
                break;
        }
    }

    @BindingAdapter("android:passwordError")
    public static void setPasswordErrorMessage(TextInputLayout view, LoginResult.PasswordError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case INCORRECT:
                view.setError(view.getContext().getString(R.string.wrong_password));
                break;
        }
    }

    @BindingAdapter("android:regUsernameError")
    public static void setRegUsernameErrorMessage(TextInputLayout view, RegisterResult.UsernameError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case EMPTY:
                view.setError(view.getContext().getString(R.string.username_req));
                break;
            case EXISTS:
                view.setError(view.getContext().getString(R.string.invalid_username));
                break;
        }
    }

    @BindingAdapter("android:regPasswordError")
    public static void setRegPasswordErrorMessage(TextInputLayout view, RegisterResult.PasswordError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case EMPTY:
                view.setError(view.getContext().getString(R.string.pwd_req));
                break;
            case INVALID:
                view.setError(view.getContext().getString(R.string.invalid_password));
                break;
        }
    }

    @BindingAdapter("android:regPasswordError2")
    public static void setRegPasswordErrorMessage2 (TextInputLayout view, RegisterResult.PasswordError2 error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case INVALID:
                view.setError(view.getContext().getString(R.string.invalid_password));
                break;
        }
    }

    @BindingAdapter("android:regEmailError")
    public static void setRegEmailErrorMessage(TextInputLayout view, RegisterResult.EmailError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case EMPTY:
                view.setError(view.getContext().getString(R.string.email_req));
                break;
            case INVALID:
                view.setError(view.getContext().getString(R.string.email_taken));
                break;
        }
    }

    @BindingAdapter("android:regBirthdayError")
    public static void setRegBirthdayErrorMessage(TextInputLayout view, RegisterResult.BirthdayError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case INVALID:
                view.setError(view.getContext().getString(R.string.invalid_date));
                break;
        }
    }
}