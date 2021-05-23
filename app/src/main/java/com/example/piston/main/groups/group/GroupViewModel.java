package com.example.piston.main.groups.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.posts.Post;
import com.example.piston.utilities.Values;

import java.util.ArrayList;

public class GroupViewModel extends ViewModel implements GroupRepository.IGroup {

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> fromShareJoinedGroup = new MutableLiveData<>(false);
    private final MutableLiveData<String> filter = new MutableLiveData<>(Values.FILTER_DEFAULT);
    private final MutableLiveData<Boolean> modMode = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> priority = new MutableLiveData<>(2);

    private final GroupRepository repository;

    public GroupViewModel(String group) {
        repository = new GroupRepository(this, group);
    }

    public void updateFilter(String filter, boolean descending) {
        repository.updateQuery(filter, descending);
    }

    public void fromShareJoinGroup(String groupID) {
        repository.fromShareJoinGroup(groupID);
    }

    @Override
    public void setFilter(String filter) {
        this.filter.setValue(filter);
    }

    @Override
    public void setPermissions(boolean modMode, Integer priority) {
        this.modMode.setValue(modMode);
        this.priority.setValue(priority);
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

    public LiveData<Boolean> getModMode() {
        return modMode;
    }

    public LiveData<String> getFilter() {
        return filter;
    }

    public LiveData<Integer> getPriority() {
        return priority;
    }
}
