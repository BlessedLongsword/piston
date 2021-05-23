package com.example.piston.main.global.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.posts.Post;
import com.example.piston.utilities.Values;

import java.util.ArrayList;

public class CategoryViewModel extends ViewModel implements CategoryRepository.ICategory {

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> filter = new MutableLiveData<>(Values.FILTER_DEFAULT);

    private final CategoryRepository repository;

    public CategoryViewModel(String category) {
        repository = new CategoryRepository(this, category);
    }

    public void updateFilter(String filter, boolean descending) {
        repository.updateQuery(filter, descending);
    }

    @Override
    public void setFilter(String filter) {
        this.filter.setValue(filter);
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

    public LiveData<String> getFilter() {
        return filter;
    }


}
