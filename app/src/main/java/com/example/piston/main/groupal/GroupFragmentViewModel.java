package com.example.piston.main.groupal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Section;

import java.util.ArrayList;

public class GroupFragmentViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Section>> groups;
    private final ArrayList<Section> groups_array;

    public GroupFragmentViewModel() {
        groups_array = new ArrayList<>();
        groups = new MutableLiveData<>(groups_array);
    }

    public LiveData<ArrayList<Section>> getGroups() {
        return groups;
    }

    public void createGroup(String title, String description){
        //categoryManager.createFolder(title, description);
        //folders.postValue(categoryManager.getFolderNames());
        groups_array.add(new Section(title, description));
        groups.setValue(groups_array);
    }
}
