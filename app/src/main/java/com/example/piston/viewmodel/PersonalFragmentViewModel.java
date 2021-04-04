package com.example.piston.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class PersonalFragmentViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<String>> folders;
    //private final CategoryManager categoryManager;
    private final ArrayList<String> folders_array;

    public PersonalFragmentViewModel() {
        folders = new MutableLiveData<>();
        folders_array = new ArrayList<>();
        //categoryManager = new CategoryManager();
    }

    public LiveData<ArrayList<String>> getFolders() {
        return folders;
    }

    public void createFolder(String title, String description){
        //categoryManager.createFolder(title, description);
        //folders.postValue(categoryManager.getFolderNames());
        folders_array.add(title);
        folders.setValue(folders_array);
    }

}
