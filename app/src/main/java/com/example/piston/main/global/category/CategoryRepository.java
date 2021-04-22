package com.example.piston.main.global.category;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class CategoryRepository {

    public interface ICategory {
        void setCategoryPosts(ArrayList<Post> posts);
    }

    private final ICategory listener;

    public CategoryRepository(ICategory listener) {
        this.listener = listener;
    }

    public void loadCategoryPosts(String category) {
        listener.setCategoryPosts(new ArrayList<>());
    }

}
