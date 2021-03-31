package com.example.piston.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.PostStorage;
import com.example.piston.model.Result;
import com.example.piston.model.User;
import com.example.piston.model.UserList;
import com.example.piston.R;

import java.util.Date;

public class PistonViewModel extends ViewModel {

    private MutableLiveData<PostStorage> postStorageMLD;
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    private String currentUser;
    private UserList users;

    public PistonViewModel() {
        postStorageMLD = new MutableLiveData<>();
        users = new UserList();
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<User> result = users.login(username, password);
        Log.d("NoWayBro", result.toString());

        if (result instanceof Result.Success) {
            User user = ((Result.Success<User>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(user.getName())));
            Log.d("NoWayBro", "Princ");
        } else {
            if (result.toString().equals("Error[exception=Wrong password]")) {
                loginResult.setValue(new LoginResult(R.string.wrong_password));
                Log.d("NoWayBro", "Dins");
            }
            else if (result.toString().equals("Error[exception=This user does not exist]")) {
                loginResult.setValue(new LoginResult(R.string.wrong_user));
                Log.d("NoWayBro", "Noooo");
            }
        }
        Log.d("NoWayBro", "Final");
    }

    public LiveData<PostStorage> getPostStorageMLD() {
        return postStorageMLD;
    }

    public void registerUser(String username, String pwd1, String pwd2, String email, Date birthDate) throws Exception {
        users.registerUser(username,pwd1,pwd2,email,birthDate);

    }

    public void loginUser(String username, String pwd) throws Exception {
       users.loginUser(username,pwd);
       currentUser = username;

    }

    public void loginDataChanged(String username, String password) {

    }
}
