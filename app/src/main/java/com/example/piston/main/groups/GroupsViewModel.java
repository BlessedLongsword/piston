package com.example.piston.main.groups;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Group;
import com.example.piston.data.Section;
import com.example.piston.main.global.GlobalRepository;

import java.util.ArrayList;

public class GroupsViewModel extends ViewModel implements GroupsRepository.IGroup {

    private final MutableLiveData<ArrayList<Group>> groups = new MutableLiveData<>(new ArrayList<>());

    public GroupsViewModel() {
        GroupsRepository groupsRepository = new GroupsRepository(this);
        groupsRepository.loadGroups();
    }

    public LiveData<ArrayList<Group>> getGroups() {
        return groups;
    }

    @Override
    public void setGroups(ArrayList<Group> groups) {
        this.groups.setValue(groups);
    }
}
