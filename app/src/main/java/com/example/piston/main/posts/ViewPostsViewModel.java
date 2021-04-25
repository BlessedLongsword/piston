package com.example.piston.main.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class ViewPostsViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Post>> posts;
    private final ArrayList<Post> posts_array;

    public ViewPostsViewModel() {
        posts_array = new ArrayList<>();
        posts = new MutableLiveData<>(posts_array);
    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }

    /*
    public void createPost(String title, String owner, String description){
        posts_array.add(new Post(title, owner, description));
        posts.setValue(posts_array);
    }
     */

}