package com.example.piston.main.global.category;

import com.example.piston.data.Post;

import java.util.ArrayList;

public class CategoryRepository {

    public interface CategoryListener {
        void setCategoryPosts(ArrayList<Post> posts);
    }

    private final CategoryListener listener;

    public CategoryRepository(CategoryListener listener) {
        this.listener = listener;
    }

    public void loadCategoryPosts(String category) {
        listener.setCategoryPosts(new ArrayList<>());
    }

}
