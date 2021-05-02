package com.example.piston.main.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class PostViewModel extends ViewModel implements PostRepository.IPosts{

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> postTitle = new MutableLiveData<>("");

    private final PostRepository repository;

    public PostViewModel(String collection, String document, String postID) {
        repository = new PostRepository(this, collection, document, postID);
    }

    @Override
    public void setPosts(ArrayList<Post> posts) {
        this.posts.setValue(posts);
    }

    @Override
    public void setPostTitle(String title) {
        this.postTitle.setValue(title);
    }

    @Override
    protected void onCleared () {
        repository.removeListener();
    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }
    public LiveData<String> getPostTitle() { return postTitle; }

}