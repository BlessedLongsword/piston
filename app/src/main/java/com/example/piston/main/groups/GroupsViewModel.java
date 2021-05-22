package com.example.piston.main.groups;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.sections.Group;
import com.example.piston.utilities.Values;

import java.util.ArrayList;

public class GroupsViewModel extends ViewModel implements GroupsRepository.IGroup {

    private final MutableLiveData<ArrayList<Group>> groups = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> filter = new MutableLiveData<>(Values.FILTER_DEFAULT);
    private final GroupsRepository repository = new GroupsRepository(this);

    public LiveData<ArrayList<Group>> getGroups() {
        return groups;
    }

    public void updateFilter(String filter, boolean descending) {
        repository.updateQuery(filter, descending);
    }

    @Override
    public void setGroups(ArrayList<Group> groups) {
        this.groups.setValue(groups);
    }

    @Override
    public void setFilter(String filter) {
        this.filter.setValue(filter);
    }

    public void removeListener() {
        repository.removeListener();
    }

    public LiveData<String> getFilter() {
        return filter;
    }
}
