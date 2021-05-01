package com.example.piston.main.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class PostsViewModel extends ViewModel implements PostsRepository.IPosts{

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());

    private final PostsRepository repository;

    public PostsViewModel(String collection, String document, String postID) {
        repository = new PostsRepository(this, collection, document, postID);
    }

    @Override
    public void setPosts(ArrayList<Post> posts) {
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