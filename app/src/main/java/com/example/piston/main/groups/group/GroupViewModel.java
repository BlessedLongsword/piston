package com.example.piston.main.groups.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class GroupViewModel extends ViewModel implements GroupRepository.IGroup {

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());

    private final GroupRepository repository;

    public GroupViewModel(String Group) {
        repository = new GroupRepository(this, Group);
    }

    @Override
    public void setGroupPosts(ArrayList<Post> posts) {
        this.posts.setValue(posts);
    }

    @Override
    protected void onCleared () {
        repository.removeListener();
    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }
}
