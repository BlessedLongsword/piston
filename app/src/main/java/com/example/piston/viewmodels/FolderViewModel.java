package com.example.piston.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class FolderViewModel {
    private final MutableLiveData<ArrayList<Post>> posts;
    private final ArrayList<Post> post_array;


    public FolderViewModel() {
        post_array = new ArrayList<>();
        posts = new MutableLiveData<>(post_array);

    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }

    public void createPost(String title, String text, String owner, int id){
        post_array.add(new Post(title, text, owner, id));
        posts.setValue(post_array);
    }
}

