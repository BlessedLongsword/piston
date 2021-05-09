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
    private final MutableLiveData<Boolean> liked = new MutableLiveData<>(false);

    private final PostRepository repository;

    public PostViewModel(String collection, String document, String postID) {
        repository = new PostRepository(this, collection, document, postID);
        repository.checkLiked(postID);
    }

    public void createReply(String content) {
        repository.createReply(content);
    }

    public void createReply(String content, String quote, String quoteOwner, String quoteID) {
        repository.createReply(content, quote, quoteOwner, quoteID);
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
    public void setIsLiked(boolean liked){
        this.liked.setValue(liked);
    }


    public void setLiked(Boolean bool){
        this.liked.setValue(bool);
        repository.addLiked(bool,post.getValue().getId());
        repository.updateNumLikes(bool);
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

    public LiveData<Boolean> getLiked() {return liked;}

}