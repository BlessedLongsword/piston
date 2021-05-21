package com.example.piston.main.groups;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.sections.Group;

import java.util.ArrayList;

public class GroupsViewModel extends ViewModel implements GroupsRepository.IGroup {

    private final MutableLiveData<ArrayList<Group>> groups = new MutableLiveData<>(new ArrayList<>());
    private final GroupsRepository repository = new GroupsRepository(this);

    public LiveData<ArrayList<Group>> getGroups() {
        return groups;
    }

    public void setFilter(String filter) {
        repository.updateQuery(filter);
    }

    @Override
    public void setGroups(ArrayList<Group> groups) {
        this.groups.setValue(groups);
    }

    public void removeListener() {
        repository.removeListener();
    }
}
