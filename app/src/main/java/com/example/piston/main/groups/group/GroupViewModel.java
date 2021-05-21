package com.example.piston.main.groups.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.posts.Post;

import java.util.ArrayList;

public class GroupViewModel extends ViewModel implements GroupRepository.IGroup {

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> fromShareJoinedGroup = new MutableLiveData<>(false);

    private final GroupRepository repository;

    public GroupViewModel(String group) {
        repository = new GroupRepository(this, group);
    }

    public void setFilter(String filter) {
        repository.updateQuery(filter);
    }

    public void fromShareJoinGroup(String groupID) {
        repository.fromShareJoinGroup(groupID);
    }

    @Override
    public void setGroupPosts(ArrayList<Post> posts) {
        this.posts.setValue(posts);
    }

    @Override
    public void setTitle(String title) { this.title.setValue(title); }

    @Override
    public void setFromShareJoinedGroup() {
        fromShareJoinedGroup.setValue(true);
    }

    @Override
    protected void onCleared () {
        repository.removeListener();
    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }

    public LiveData<String> getTitle() { return title; }

    public LiveData<Boolean> getFromShareJoinedGroup() {
        return fromShareJoinedGroup;
    }
}
