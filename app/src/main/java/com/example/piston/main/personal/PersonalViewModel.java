package com.example.piston.main.personal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Folder;
import com.example.piston.main.personal.PersonalRepository;

import java.util.ArrayList;

public class PersonalViewModel extends ViewModel implements PersonalRepository.IPersonal{

    private final MutableLiveData<ArrayList<Folder>> folders = new MutableLiveData<>(new ArrayList<>());

    public PersonalViewModel() {
        PersonalRepository personalRepository = new PersonalRepository(this);
        personalRepository.loadFolders();
    }

    public LiveData<ArrayList<Folder>> getFolders() {
        return folders;
    }

    @Override
    public void setFolders(ArrayList<Folder> folders) {
        this.folders.setValue(folders);
    }

}
