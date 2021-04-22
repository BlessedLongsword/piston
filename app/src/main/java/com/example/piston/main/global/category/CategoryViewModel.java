package com.example.piston.main.global.category;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class CategoryViewModel extends ViewModel implements CategoryRepository.ICategory {

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());

    private final CategoryRepository repository = new CategoryRepository(this);

    public void setTitle(String category) {
        repository.loadCategoryPosts(category);
    }

    @Override
    public void setCategoryPosts(ArrayList<Post> posts) {
        this.posts.setValue(posts);
    }
}
