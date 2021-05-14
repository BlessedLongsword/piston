package com.example.piston.main.groups.group.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.GroupMember;

import java.util.ArrayList;

public class GroupInfoViewModel extends ViewModel implements GroupInfoRepository.IGroupInfo {

    private final MutableLiveData<String> id = new MutableLiveData<>("");
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> description = new MutableLiveData<>("");
    private final MutableLiveData<String> imageLink = new MutableLiveData<>("");
    private final MutableLiveData<String> numberOfMembers = new MutableLiveData<>("");
    private final MutableLiveData<ArrayList<GroupMember>> members = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isOwner = new MutableLiveData<>(false);

    final GroupInfoRepository repository;

    public GroupInfoViewModel(String group) {
        id.setValue(group);
        repository = new GroupInfoRepository(this, id.getValue());
    }

    @Override
    public void setParams(String title, String description, String imageLink, String groupID) {
        this.title.setValue(title);
        this.description.setValue(description);
        this.imageLink.setValue(imageLink);
        id.setValue(groupID);
    }

    @Override
    public void setMembers(ArrayList<GroupMember> members) {
        this.members.setValue(members);
        numberOfMembers.setValue(Integer.toString(members.size()));
    }

    @Override
    public void setIsOwner(boolean priority) {
        this.isOwner.setValue(priority);
    }

    public void deleteGroup() {
        repository.deleteGroup();
    }

    public LiveData<String> getID() { return id; }

    public LiveData<String> getTitle() { return title; }

    public LiveData<String> getDescription() {
        return description;
    }

    public LiveData<String> getImageLink() {
        return imageLink;
    }

    public LiveData<ArrayList<GroupMember>> getMembers() { return members; }

    public LiveData<String> getNumberOfMembers() { return numberOfMembers; }

    public LiveData<Boolean> getIsOwner() {
        return isOwner;
    }
}
