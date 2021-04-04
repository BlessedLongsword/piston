package com.example.piston.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.R;
import com.example.piston.model.Result;
import com.example.piston.model.User;
import com.example.piston.model.UserList;

public class LoginActivityViewModel extends ViewModel {

    private MutableLiveData<LoginResult> loginResult;
    private UserList users;

    public LoginActivityViewModel() {
        loginResult = new MutableLiveData<>();
        users = new UserList();
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<User> result = users.login(username, password);

        if (result instanceof Result.Success) {
            User user = ((Result.Success<User>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(user.getName())));
        } else {
            if (result.toString().equals("Error[exception=Wrong password]")) {
                loginResult.setValue(new LoginResult(R.string.wrong_password));
            }
            else if (result.toString().equals("Error[exception=This user does not exist]")) {
                loginResult.setValue(new LoginResult(R.string.wrong_user));
            }
        }
    }
}
