package com.example.piston.main.posts;

import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piston.data.posts.Reply;

import java.util.ArrayList;
import java.util.Objects;

public class PostViewModel extends ViewModel implements PostRepository.IPosts{

    private final MutableLiveData<ArrayList<Reply>> replies = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> postTitle = new MutableLiveData<>("");
    private final MutableLiveData<String> postOwner = new MutableLiveData<>("");
    private final MutableLiveData<String> postOwnerEmail = new MutableLiveData<>("");
    private final MutableLiveData<String> postContent = new MutableLiveData<>("");
    private final MutableLiveData<String> postImageLink = new MutableLiveData<>("");
    private final MutableLiveData<String> profileImageLink = new MutableLiveData<>("");
    private final MutableLiveData<String> currentUser = new MutableLiveData<>("");

    private final MutableLiveData<Integer> priority = new MutableLiveData<>(2);
    private final MutableLiveData<String> numLikes = new MutableLiveData<>( "0");

    private final MutableLiveData<Boolean> postDoesNotExist = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> liked = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loaded = new MutableLiveData<>(false);
    private boolean firstLoad = false;

    private final PostRepository repository;
    private final String scope;


    public PostViewModel(String scope, String sectionID, String postID) {
        repository = new PostRepository(this, scope, sectionID, postID);
        repository.checkLiked(postID);
        this.scope = scope;
    }

    public void createReply(String content) {
        repository.createReply(content);
    }

    public void createReply(String content, String quote, String quoteOwner, String quoteID) {
        repository.createReply(content, quote, quoteOwner, quoteID);
    }

    public void deleteReply(String replyID) {
        repository.deleteReply(replyID);
    }

    public void editReply(String replyID, String content) {
        repository.editReply(replyID, content);
    }

    public void updatePost() {
        repository.updatePost();
    }

    @Override
    public void setReplies(ArrayList<Reply> replies) {
        this.replies.setValue(replies);
    }

    @Override
    public void setPostParams(String title, String owner, String ownerEmail, String content,
                              String postImageLink, String profileImageLink) {
        this.postTitle.setValue(title);
        this.postOwner.setValue(owner);
        this.postOwnerEmail.setValue(ownerEmail);
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
    public void setPriority(Integer priority) {
        this.priority.setValue(Math.min(priority, Objects.requireNonNull(this.priority.getValue())));
    }

    @Override
    public void setIsLiked(boolean liked){
        this.liked.setValue(liked);
    }

    @Override
    public void setPostDoesNotExist() {
        this.postDoesNotExist.setValue(true);
    }

    @Override
    public void setCurrentUser(String currentUser) {
        this.currentUser.setValue(currentUser);
    }

    @Override
    public void setNumLikes(String numLikes) {
        this.numLikes.setValue(numLikes);
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

    public LiveData<String> getPostOwnerEmail() {
        return postOwnerEmail;
    }

    public LiveData<String> getPostContent() {
        return postContent;
    }

    public LiveData<String> getPostImageLink() {
        return postImageLink;
    }

    public LiveData<String> getProfileImageLink() {
        return profileImageLink;
    }

    public void deletePost() {
        repository.deletePost();
    }

    public LiveData<Integer> getPriority() {
        return priority;
    }

    public LiveData<Boolean> getPostDoesNotExist() { return postDoesNotExist; }

    public LiveData<String> getCurrentUser() { return currentUser; }

    public String getScope() { return scope; }

    public LiveData<String> getNumLikes() {
        return numLikes;
    }
}