package com.example.piston.util;

import androidx.databinding.BindingAdapter;

import com.example.piston.R;
import com.example.piston.model.User;
import com.google.android.material.textfield.TextInputLayout;

public class BindingAdapters {

    @BindingAdapter("android:usernameError")
    public static void setErrorMessage(TextInputLayout view, User.UsernameError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case EMPTY:
                view.setError(view.getContext().getString(R.string.username_req));
                break;
            case INVALID:
                view.setError(view.getContext().getString(R.string.wrong_user));

        }
    }
}