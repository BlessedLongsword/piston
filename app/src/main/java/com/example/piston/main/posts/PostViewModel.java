package com.example.piston.main.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Post;
import com.example.piston.data.Reply;

import java.util.ArrayList;

public class PostViewModel extends ViewModel implements PostRepository.IPosts{

    private final MutableLiveData<Post> post = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Reply>> replies = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> postTitle = new MutableLiveData<>("");

    private final PostRepository repository;

    public PostViewModel(String collection, String document, String postID) {
        repository = new PostRepository(this, collection, document, postID);
    }

    @Override
    public void setReplies(ArrayList<Reply> replies) {
        this.replies.setValue(replies);
    }

    @Override
    public void setPost(Post post) {
        this.post.setValue(post);
    }

    @Override
    public void setPostTitle(String title) {
        this.postTitle.setValue(title);
    }

    @Override
    protected void onCleared () {
        repository.removeListener();
    }

    public LiveData<ArrayList<Reply>> getReplies() {
        return replies;
    }
    
    public LiveData<String> getPostTitle() { return postTitle; }
    
    public LiveData<Post> getPost() { return post; }

}