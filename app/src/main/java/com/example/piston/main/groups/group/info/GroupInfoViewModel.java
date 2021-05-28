package com.example.piston.main.groups.group.info;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.users.GroupMember;

import java.util.ArrayList;

public class GroupInfoViewModel extends ViewModel implements GroupInfoRepository.IGroupInfo {

    private final MutableLiveData<String> id = new MutableLiveData<>("");
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> description = new MutableLiveData<>("");
    private final MutableLiveData<String> imageLink = new MutableLiveData<>("");
    private final MutableLiveData<String> numberOfMembers = new MutableLiveData<>("");
    private final MutableLiveData<ArrayList<GroupMember>> members = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> priority = new MutableLiveData<>(2);
    private final MutableLiveData<Boolean> modMode = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> finished = new MutableLiveData<>(false);

    private final GroupInfoRepository repository;

    public GroupInfoViewModel(String group) {
        id.setValue(group);
        repository = new GroupInfoRepository(this, id.getValue());
    }

    public void setImage(Uri image) {
        repository.setImage(image);
    }

    @Override
    public void setImageLink(String imageLink) {
        this.imageLink.setValue(imageLink);
    }

    @Override
    public void setParams(String title, String description, String imageLink, String groupID, boolean modMode) {
        this.title.setValue(title);
        this.description.setValue(description);
        this.imageLink.setValue(imageLink);
        this.modMode.setValue(modMode);
        id.setValue(groupID);
    }

    @Override
    public void setMembers(ArrayList<GroupMember> members) {
        this.members.setValue(members);
        numberOfMembers.setValue(Integer.toString(members.size()));
    }

    @Override
    public void setPriority(Integer priority) {
        this.priority.setValue(priority);
    }

    @Override
    public void setFinished() {
        this.finished.setValue(true);
    }

    public void removeMember(String memberEmail) {
        repository.removeMember(memberEmail);
    }

    public void updateMemberPriority(String memberEmail, int priority) {
        repository.updateMemberPriority(memberEmail, priority);
    }

    @Override
    protected void onCleared() {
        repository.removeListener();
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

    public LiveData<Integer> getPriority() {
        return priority;
    }

    public LiveData<Boolean> getModMode() {
        return modMode;
    }

    public void editDescription(String text) {
        repository.editDescription(text);
    }

    public void update() {
        repository.updateParams();
    }

    public LiveData<Boolean> getFinished() {
        return finished;
    }

    public void editTitle(String text) {
        repository.editTitle(text);
    }

    public void setModMode(boolean modMode) { repository.setModMode(modMode); }

    public void reset() {
        finished.setValue(false);
    }
}
