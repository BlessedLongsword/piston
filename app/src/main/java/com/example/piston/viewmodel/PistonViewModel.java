package com.example.piston.viewmodel;

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

    private String currentUser;
    private UserList users;
    private CategoryManager categoryManager;

    public PistonViewModel() {
        activeTab = new MutableLiveData<>();
        folderChooser = new MutableLiveData<>();
        loginResult = new MutableLiveData<>();
        users = new UserList();
        categoryManager = new CategoryManager();
        setActiveTab(0);
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

    public void registerUser(String username, String pwd1, String pwd2, String email, Date birthDate) throws Exception {
        users.registerUser(username,pwd1,pwd2,email,birthDate);

    }

    public MutableLiveData<ArrayList<String>> getFolderChooser() {
        return folderChooser;
    }

    public void setFolderChooser(String title, String description) {
        categoryManager.createFolder(title, description);
        this.folderChooser.postValue(categoryManager.getFolderNames());
    }

    public Integer getActiveTab() {
        return activeTab.getValue();
    }

    public void setActiveTab(int activeTab) {
        this.activeTab.setValue(activeTab);
    }
}
