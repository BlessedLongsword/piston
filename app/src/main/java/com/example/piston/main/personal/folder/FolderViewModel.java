package com.example.piston.main.personal.folder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.posts.Post;

import java.util.ArrayList;

public class FolderViewModel extends ViewModel implements FolderRepository.IFolder {

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> title = new MutableLiveData<>("");

    private final FolderRepository repository;

    public FolderViewModel(String folder) {
        repository = new FolderRepository(this, folder);
    }

    public void setFilter(String filter) {
        repository.updateQuery(filter);
    }

    @Override
    public void setFolderPosts(ArrayList<Post> posts) {
        this.posts.setValue(posts);
    }

    @Override
    public void setTitle(String title) {
        this.title.setValue(title);
    }

    @Override
    protected void onCleared () {
        repository.removeListener();
    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }

    public LiveData<String> getTitle() {
        return title;
    }
}
