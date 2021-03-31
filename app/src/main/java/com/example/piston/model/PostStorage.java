package com.example.piston.model;

import java.util.HashMap;
import java.util.Map;

public class PostStorage {

    Map posts;

    public PostStorage() {
        posts = new HashMap<String, Post>();
    }

    public void createPost(String title, String content, String picturePath) {
        Post post = new Post(title, content, picturePath, posts.size());
        posts.put(post.getId(), post);
    }

}
