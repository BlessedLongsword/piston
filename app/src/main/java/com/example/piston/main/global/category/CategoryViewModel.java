package com.example.piston.main.global.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class CategoryViewModel extends ViewModel implements CategoryRepository.ICategory {

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());

    private final CategoryRepository repository;

    public CategoryViewModel(String category) {
        repository = new CategoryRepository(this, category);
    }

    @Override
    public void setCategoryPosts(ArrayList<Post> posts) {
        this.posts.setValue(posts);
    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }

    public void removeListener() {
        repository.removeListener();
    }
}
