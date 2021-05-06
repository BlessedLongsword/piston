package com.example.piston.utilities;

import android.view.View;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

import com.example.piston.R;
import com.example.piston.main.global.createCategory.CreateCategoryResult;
import com.example.piston.main.groups.createGroup.CreateGroupResult;
import com.example.piston.main.groups.joinGroup.JoinGroupResult;
import com.example.piston.main.personal.createFolder.CreateFolderResult;
import com.example.piston.authentication.login.LoginResult;
import com.example.piston.main.posts.createPost.CreatePostResult;
import com.example.piston.main.profile.ProfileResult;
import com.example.piston.authentication.register.RegisterResult;
import com.example.piston.utilities.textwatchers.CounterWatcher;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

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
    public static void setRegConfirmPasswordErrorMessage(TextInputLayout view, RegisterResult.ConfirmPasswordError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case INVALID:
                view.setError(view.getContext().getString(R.string.passwords_dont_match));
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
                view.setError(view.getContext().getString(R.string.email_invalid));
                break;
            case EXISTS:
                view.setError(view.getContext().getString(R.string.email_taken));
                break;
            case UNEXPECTED:
                view.setError(view.getContext().getString(R.string.unexpected_error));
                break;
        }
    }

    @BindingAdapter("android:regBirthDateError")
    public static void setRegBirthDateErrorMessage(TextInputLayout view, RegisterResult.BirthDateError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case INVALID:
                view.setError(view.getContext().getString(R.string.invalid_date));
                break;
        }
    }

    @BindingAdapter("android:profileBirthdayError")
    public static void setProfileBirthdayErrorMessage(TextInputLayout view, ProfileResult.BirthDateError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case INVALID:
                view.setError(view.getContext().getString(R.string.invalid_date));
                break;
        }
    }

    @BindingAdapter("android:onFocusLost")
    public static void onFocusChange(EditText text, Runnable runnable) {
        text.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus)
                runnable.run();
        });
    }

    @BindingAdapter("android:setVisible")
    public static void setVisible(View view, boolean visible) {
        view.setVisibility((visible)? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("android:categoryTitleError")
    public static void setCategoryTitleErrorMessage(TextInputLayout view, CreateCategoryResult.TitleError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case EMPTY:
                view.setError(view.getContext().getString(R.string.title_empty));
                break;
            case EXISTS:
                view.setError(view.getContext().getString(R.string.create_category_title_taken));
                break;
        }
    }

    @BindingAdapter("android:groupTitleError")
    public static void setGroupTitleErrorMessage(TextInputLayout view, CreateGroupResult.TitleError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case EMPTY:
                view.setError(view.getContext().getString(R.string.create_group_error_message));
                break;
        }
    }

    @BindingAdapter("android:counter")
    public static void updateCounter(TextInputLayout view, int max_length) {
        Objects.requireNonNull(view.getEditText()).addTextChangedListener(new CounterWatcher(max_length, view));
        view.getEditText().setOnFocusChangeListener((v, hasFocus) ->
                view.setSuffixText(Integer.toString(max_length - view.getEditText().getText().length())));
    }

    @BindingAdapter("android:folderTitleError")
    public static void setFolderTitleErrorMessage(TextInputLayout view, CreateFolderResult.TitleError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case EMPTY:
                view.setError(view.getContext().getString(R.string.title_empty));
                break;
            case EXISTS:
                view.setError(view.getContext().getString(R.string.create_category_title_taken));
                break;
        }
    }

    @BindingAdapter("android:postTitleError")
    public static void setPostTitleErrorMessage(TextInputLayout view, CreatePostResult.TitleError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case EMPTY:
                view.setError(view.getContext().getString(R.string.title_empty));
                break;
        }
    }

    @BindingAdapter("android:groupCodeError")
    public static void setGroupCodeError(TextInputLayout view, JoinGroupResult.JoinError error) {
        switch (error) {
            case NONE:
                view.setError(null);
                break;
            case EMPTY:
                view.setError(view.getContext().getString(R.string.title_empty));
                break;
            case NOT_EXISTS:
                view.setError(view.getContext().getString(R.string.join_group_error));
                break;
            case ALREADY_JOINED:
                view.setError(view.getContext().getString(R.string.join_group_already));
                break;
        }
    }

}