package com.example.piston.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class PersonalFragmentViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<String>> folders;
    //private final CategoryManager categoryManager;

    public PersonalFragmentViewModel() {
        Log.d("nowaybro", "Created PersonalFragmentViewModel");
        folders = new MutableLiveData<>(new ArrayList<>());
        //categoryManager = new CategoryManager();
    }

    public LiveData<ArrayList<String>> getFolders() {
        return folders;
    }

    public void createFolder(String title, String description){
        //categoryManager.createFolder(title, description);
        //folders.postValue(categoryManager.getFolderNames());
        Log.d("nowaybro", "Added: " + title);
        folders.getValue().add(title);
        for (String folder : folders.getValue())
            Log.d("nowaybro", folder);
    }

}
