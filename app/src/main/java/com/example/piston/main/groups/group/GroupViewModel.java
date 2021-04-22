package com.example.piston.main.groups.group;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class GroupViewModel {
    private final MutableLiveData<ArrayList<Post>> posts;
    private final ArrayList<Post> post_array;


    public GroupViewModel() {
        post_array = new ArrayList<>();
        posts = new MutableLiveData<>(post_array);

    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }

    public void createPost(String title, String text, String owner){
        post_array.add(new Post(title, text, owner));
        posts.setValue(post_array);
    }
}
