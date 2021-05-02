package com.example.piston.main.groups.group.info;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GroupInfoViewModel extends ViewModel implements GroupInfoRepository.IGroupInfo {

    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> description = new MutableLiveData<>("");
    private final MutableLiveData<String> imageLink = new MutableLiveData<>("");

    final GroupInfoRepository repository;

    public GroupInfoViewModel(String group) {
        repository = new GroupInfoRepository(this, group);
    }

    @Override
    public void setTitle(String title) {
        this.title.setValue(title);
    }

    @Override
    public void setDescription(String description) {
        this.description.setValue(description);
    }

    @Override
    public void setImageLink(String imageLink) {
        this.imageLink.setValue(imageLink);
    }

    public LiveData<String> getTitle() { return title; }

    public LiveData<String> getDescription() {
        return description;
    }

    public LiveData<String> getImageLink() {
        return imageLink;
    }

}
