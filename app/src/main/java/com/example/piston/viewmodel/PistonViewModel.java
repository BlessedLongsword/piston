package com.example.piston.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.model.PistonModel;
import com.example.piston.model.PostStorage;
import com.example.piston.model.User;
import com.example.piston.model.UserList;

import java.util.Date;

public class PistonViewModel extends ViewModel {

    private MutableLiveData<PostStorage> postStorageMLD;

    private String currentUser;
    private UserList users;

    public PistonViewModel() {
        postStorageMLD = new MutableLiveData<>();
        users = new UserList();
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
}
