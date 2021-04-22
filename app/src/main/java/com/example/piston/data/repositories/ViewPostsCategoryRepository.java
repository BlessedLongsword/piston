package com.example.piston.data.repositories;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class ViewPostsCategoryRepository {

    public interface CategoryListener {
        void setCategoryPosts(ArrayList<Post> posts);
    }

    private final CategoryListener listener;

    public ViewPostsCategoryRepository(CategoryListener listener) {
        this.listener = listener;
    }

    public void loadCategoryPosts(String category) {
        listener.setCategoryPosts(new ArrayList<>());
    }

}
