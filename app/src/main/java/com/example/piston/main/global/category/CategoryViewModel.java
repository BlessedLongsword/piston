package com.example.piston.main.global.category;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class CategoryViewModel extends ViewModel implements CategoryRepository.ICategory {

    private final MutableLiveData<ArrayList<Post>> posts = new MutableLiveData<>(new ArrayList<>());

    private final CategoryRepository repository = new CategoryRepository(this);

    public CategoryViewModel(String category) {
        Log.d("nowaybro", "Created");
        repository.loadCategoryPosts(category);
    }

    public void setTitle(String category) {
        repository.loadCategoryPosts(category);
    }

    @Override
    public void setCategoryPosts(ArrayList<Post> posts) {
        Log.d("nowaybro", String.valueOf(posts.size()));
        for (Post post : posts)
            Log.d("nowaybro", post.getTitle());
        this.posts.setValue(posts);
    }

    public LiveData<ArrayList<Post>> getPosts() {
        return posts;
    }
}
