package com.example.piston.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.LoginRepository;
import com.example.piston.model.LoginResult;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class LoginViewModelActivity extends ViewModel implements LoginRepository.ILogin {

    private final MutableLiveData<String> username = new MutableLiveData<>("");
    private final MutableLiveData<String> password = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> signInEnabled = new MutableLiveData<>(false);

    public final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>(new LoginResult());

    private final LoginRepository loginRepository = new LoginRepository(this);

    public void onTextChanged() {
        LiveData<String> a;
        getSignInEnabled().setValue(Objects.requireNonNull(getUsername().getValue()).length()>0 &&
                Objects.requireNonNull(getPassword().getValue()).length()>0);
    }

    public void login() {
        loginRepository.login(Objects.requireNonNull(getUsername().getValue()),
                Objects.requireNonNull(getPassword().getValue()));
    }

    public void signInWithGoogle(Task<GoogleSignInAccount> task) {
        loginRepository.signInWithGoogle(task);
    }

    @Override
    public void setLoginResult(LoginResult loginResult) {
        this.loginResult.setValue(loginResult);
        Log.d("nowaybro", loginResult.getUsernameError().toString());
        Log.d("nowaybro", loginResult.getPasswordError().toString());
    }


    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<Boolean> getSignInEnabled() {
        return signInEnabled;
    }

    public LoginResult.PasswordError getPasswordError() {
        return Objects.requireNonNull(loginResult.getValue()).getPasswordError();
    }

    public boolean isSignedIn() {
        return Objects.requireNonNull(loginResult.getValue()).isSignedIn();
    }

    public boolean isNewuser() {
        return Objects.requireNonNull(loginResult.getValue()).isNewUser();
    }
}
