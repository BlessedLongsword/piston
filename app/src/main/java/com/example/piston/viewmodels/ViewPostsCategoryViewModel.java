package com.example.piston.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Post;
import com.example.piston.data.repositories.ViewPostsCategoryRepository;

import java.util.ArrayList;

public class ViewPostsCategoryViewModel extends ViewModel implements ViewPostsCategoryRepository.CategoryListener {

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());

    private final ViewPostsCategoryRepository repository = new ViewPostsCategoryRepository(this);

    public void setTitle(String category) {
        repository.loadCategoryPosts(category);
    }

    @Override
    public void setCategoryPosts(ArrayList<Post> posts) {
        this.posts.setValue(posts);
    }
}
