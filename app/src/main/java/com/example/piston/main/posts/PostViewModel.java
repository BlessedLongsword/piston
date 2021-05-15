package com.example.piston.main.posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.Reply;

import java.util.ArrayList;

public class PostViewModel extends ViewModel implements PostRepository.IPosts{

    private final MutableLiveData<ArrayList<Reply>> replies = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> postTitle = new MutableLiveData<>("");
    private final MutableLiveData<String> postOwner = new MutableLiveData<>("");
    private final MutableLiveData<String> postContent = new MutableLiveData<>("");
    private final MutableLiveData<String> postImageLink = new MutableLiveData<>("");
    private final MutableLiveData<String> profileImageLink = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> postDoesNotExist = new MutableLiveData<>(false);

    private final MutableLiveData<Boolean> liked = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loaded = new MutableLiveData<>(false);
    private boolean firstLoad = false;

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
    public void setPostParams(String title, String owner, String content, String postImageLink, String profileImageLink) {
        this.postTitle.setValue(title);
        this.postOwner.setValue(owner);
        this.postContent.setValue(content);
        this.postImageLink.setValue(postImageLink);
        this.profileImageLink.setValue(profileImageLink);

    }

    @Override
    public void setLoaded() {
        if (!firstLoad) {
            loaded.setValue(true);
            firstLoad = true;
        }
    }

    @Override
    public void setIsLiked(boolean liked){
        this.liked.setValue(liked);
    }

    @Override
    public void setPostDoesNotExist() {
        this.postDoesNotExist.setValue(true);
    }


    public void setLiked(Boolean bool){
        this.liked.setValue(bool);
        repository.addLiked(bool);
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

    public LiveData<Boolean> getLiked() {return liked;}

    public LiveData<Boolean> getLoaded() { return loaded; }

    public LiveData<String> getPostOwner() {
        return postOwner;
    }

    public LiveData<String> getPostContent() {
        return postContent;
    }

    public LiveData<String> getPostImageLink() {
        return postImageLink;
    }

    public MutableLiveData<String> getProfileImageLink() {
        return profileImageLink;
    }

    public LiveData<Boolean> getPostDoesNotExist() { return postDoesNotExist; }
}