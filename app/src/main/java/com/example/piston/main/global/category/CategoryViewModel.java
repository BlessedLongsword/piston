package com.example.piston.main.global.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.posts.Post;

import java.util.ArrayList;

public class CategoryViewModel extends ViewModel implements CategoryRepository.ICategory {

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> title = new MutableLiveData<>("");

    private final CategoryRepository repository;

    public CategoryViewModel(String category) {
        repository = new CategoryRepository(this, category);
    }

    public void setFilter(String filter) {
        repository.updateQuery(filter);
    }

    @Override
    public void setCategoryPosts(ArrayList<Post> posts) {
        this.posts.setValue(posts);
    }

    @Override
    protected void onCleared () {
        repository.removeListener();
    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }

    @Override
    public void setTitle(String title){
        this.title.setValue(title);
    }

    public LiveData<String> getTitle() { return title; }


}
