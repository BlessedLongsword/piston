package com.example.piston.viewmodel;

import android.util.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<Integer> activeTab;

    private String currentUser;
    private UserList users;

    public MainActivityViewModel() {
        activeTab = new MutableLiveData<>();
        users = new UserList();
        setActiveTab(0);
        Log.d("Test3", "I am created");
    }

    public Integer getActiveTab () {
        return activeTab.getValue();
    }

    public void setActiveTab ( int activeTab) {
        this.activeTab.setValue(activeTab);
    }


}
