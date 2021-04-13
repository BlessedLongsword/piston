package com.example.piston.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.AuthRepository;
import com.example.piston.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class LoginActivityViewModel extends ViewModel {

    public MutableLiveData<String> username = new MutableLiveData<>("");
    public MutableLiveData<String> password = new MutableLiveData<>("");

    private MutableLiveData<User.UsernameError> usernameError = new MutableLiveData<>();
    private MutableLiveData<User.PasswordError> passwordError = new MutableLiveData<>();

    public MutableLiveData<Boolean> signInEnabled = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> signedIn = new MutableLiveData<>(false);

    private final AuthRepository authRepository = new AuthRepository(signedIn);

    public void onTextChanged() {
        signInEnabled.setValue(Objects.requireNonNull(username.getValue()).length()>0 &&
                Objects.requireNonNull(password.getValue()).length()>0);
    }

    public void login() {
        authRepository.login(Objects.requireNonNull(username.getValue()), password.getValue());
    }

    public void signInWithGoogle(Task<GoogleSignInAccount> task) {
        authRepository.signInWithGoogle(task);
    }
}
