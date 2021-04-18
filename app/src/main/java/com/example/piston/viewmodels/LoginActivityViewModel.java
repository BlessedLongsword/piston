package com.example.piston.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.LoginRepository;
import com.example.piston.data.LoginResult;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class LoginActivityViewModel extends ViewModel implements LoginRepository.ILogin {

    private final MutableLiveData<String> username = new MutableLiveData<>("");
    private final MutableLiveData<String> password = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> signInEnabled = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>(new LoginResult());

    private final LoginRepository loginRepository = new LoginRepository(this);

    public void afterTextChanged() {
        signInEnabled.setValue(Objects.requireNonNull(getUsername().getValue()).length()>0 &&
                Objects.requireNonNull(getPassword().getValue()).length()>0);
    }

    public void login() {
        loginResult.setValue(new LoginResult());
        loading.setValue(true);
        loginRepository.login(Objects.requireNonNull(getUsername().getValue()),
                Objects.requireNonNull(getPassword().getValue()));
    }

    public void signInWithGoogle(Task<GoogleSignInAccount> task) {
        loginRepository.signInWithGoogle(task);
    }

    @Override
    public void setLoginResult(LoginResult loginResult) {
        this.loginResult.setValue(loginResult);
        loading.setValue(false);
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public LiveData<Boolean> isSignInEnabled() {
        return signInEnabled;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }
}
