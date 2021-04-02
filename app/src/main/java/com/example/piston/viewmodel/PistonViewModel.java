package com.example.piston.viewmodel;

import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.CategoryManager;
import com.example.piston.model.Result;
import com.example.piston.model.User;
import com.example.piston.model.UserList;
import com.example.piston.R;

import java.util.ArrayList;
import java.util.Date;

public class PistonViewModel extends ViewModel {

    private MutableLiveData<Integer> activeTab;
    private MutableLiveData<ArrayList<String>> folderChooser;
    private MutableLiveData<LoginResult> loginResult;
    private MutableLiveData<RegisterResult> registerResult;
    private MutableLiveData<RegisterFormState> registerFormState;

    private String currentUser;
    private UserList users;
    private CategoryManager categoryManager;

    public PistonViewModel() {
        activeTab = new MutableLiveData<>();
        folderChooser = new MutableLiveData<>();
        loginResult = new MutableLiveData<>();
        registerResult = new MutableLiveData<>();
        registerFormState = new MutableLiveData<>();
        users = new UserList();
        categoryManager = new CategoryManager();
        setActiveTab(0);
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    public LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<User> result = users.login(username, password);

        if (result instanceof Result.Success) {
            User user = ((Result.Success<User>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(user.getName())));
            currentUser = user.getUsername();
        } else {
            if (result.toString().equals("Error[exception=Wrong password]")) {
                loginResult.setValue(new LoginResult(R.string.wrong_password));
            }
            else if (result.toString().equals("Error[exception=This user does not exist]")) {
                loginResult.setValue(new LoginResult(R.string.wrong_user));
            }
        }
    }

    public void register(String username, String pwd1, String pwd2, String email, Date birthDate) {
        Result<String> result = users.register(username, pwd1, pwd2, email, birthDate);

        if (result instanceof Result.Success) {
            registerResult.setValue(new RegisterResult(true));
        }
        if(result instanceof Result.Error) {
            Log.d("what"," register" + result.toString());
            if (result.toString().equals("Error[exception=Username required]")) {
                registerResult.setValue(new RegisterResult(R.string.username_req));
            }
            else if (result.toString().equals("Error[exception=Email required]")) {
                registerResult.setValue((new RegisterResult(R.string.email_req)));
            }
            else if (result.toString().equals("Error[exception=Password required]")) {
                registerResult.setValue(new RegisterResult(R.string.pwd_req));
            }
            else if (result.toString().equals("Error[exception=Username already taken]")) {
                registerResult.setValue((new RegisterResult(R.string.invalid_username)));
            }
            else if (result.toString().equals("Error[exception=This email is already taken]")) {
                registerResult.setValue((new RegisterResult(R.string.email_taken)));
            }
            else if (result.toString().equals("Error[exception=Password must be at least 6 characters]")) {
                registerResult.setValue((new RegisterResult(R.string.invalid_password)));
            }
            else if (result.toString().equals("Error[exception=Passwords do not match]")) {
                registerResult.setValue((new RegisterResult(R.string.invalid_password2)));
            }
        }
    }

    public void registerUsernameChanged(String username) {
        if (username == null) {
            return;
        }
        else if (username.trim().length() == 0){
            registerFormState.setValue(new RegisterFormState(null,null,
                    null,null, R.string.username_req,null,null));
        }
        else if (users.isValidUsername(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username,
                    null,null,null,null,null,null));
        }
    }

    public void registerPwd1Changed(String pwd1) {
        if (pwd1 == null) {
            return;
        }
        else if (pwd1.trim().length() == 0) {
            registerFormState.setValue(new RegisterFormState(null,null,
                    null,null,null, R.string.pwd_req,null));
        }
        else if (pwd1.length() < 6) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password,
                    null, null,null,null,null));
        }
    }

    public void registerPwd2Changed(String pwd1, String pwd2){
        if (pwd2 == null) {
            return;
        }
        else if (!pwd2.equals(pwd1)) {
            registerFormState.setValue(new RegisterFormState(null,null,
                    R.string.invalid_password2,null, null,null,null));
        }
    }

    public void registerEmailChanged(String email) {
        if (email == null){
            return;
        }
        else if ( !users.isValidEmail(email)) {
            registerFormState.setValue((new RegisterFormState(null,null,null,
                    null,null,null, R.string.email_req)));
        }
        else if (users.emailExists(email)) {
            registerFormState.setValue(new RegisterFormState(null,null,null,
                    R.string.email_taken,null,null,null));
        }
    }

    public MutableLiveData<ArrayList<String>> getFolderChooser () {
        return folderChooser;
    }

    public void setFolderChooser (String title, String description){
        categoryManager.createFolder(title, description);
        this.folderChooser.postValue(categoryManager.getFolderNames());
    }

    public Integer getActiveTab () {
        return activeTab.getValue();
    }

    public void setActiveTab ( int activeTab) {
        this.activeTab.setValue(activeTab);
    }
}
