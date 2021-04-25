package com.example.piston.main.personal.folder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class FolderViewModel extends ViewModel implements FolderRepository.IFolder {

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());

    private final FolderRepository repository;

    public FolderViewModel(String folder) {
        repository = new FolderRepository(this, folder);
    }

    @Override
    public void setFolderPosts(ArrayList<Post> posts) {
        this.posts.setValue(posts);
    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }
}
