package com.example.piston.main.posts.editPost;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.Objects;

public class EditPostViewModel extends ViewModel implements EditPostRepository.IEditPost {

    private final MutableLiveData<String> titleField = new MutableLiveData<>("");
    private final MutableLiveData<String> contentField = new MutableLiveData<>("");
    private final MutableLiveData<String> imageLink = new MutableLiveData<>("");
    private final MutableLiveData<EditPostResult.TitleError> titleFieldError =
            new MutableLiveData<>(EditPostResult.TitleError.NONE);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> editError = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> finishEditPost = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> errorMessage = new MutableLiveData<>(false);

    private final EditPostRepository repository;

    public EditPostViewModel(String scope, String sectionID, String postID) {
        repository = new EditPostRepository(this, scope, sectionID, postID);
    }

    public void titleUpdate() {
        repository.checkTitle(Objects.requireNonNull(titleField.getValue()));
        editError.setValue(false);
    }

    public void editPost(Uri image, boolean connected) {
        repository.editPost(Objects.requireNonNull(titleField.getValue()),
                contentField.getValue(), image, connected);
        loading.setValue(true);
    }

    @Override
    public void setTitleStatus(EditPostResult.TitleError titleError) {
        titleFieldError.setValue(titleError);
    }

    @Override
    public void setParams(String title, String content, String image) {
        titleField.setValue(title);
        contentField.setValue(content);
        imageLink.setValue(image);
    }

    @Override
    public void setEditError() {
        editError.setValue(true);
    }

    @Override
    public void setEditFinished() {
        finishEditPost.setValue(true);
    }

    @Override
    public void setLoadingFinished() {
        loading.setValue(false);
    }

    @Override
    public void setErrorMessage() {
        errorMessage.setValue(true);
    }

    public MutableLiveData<String> getTitleField() {
        return titleField;
    }

    public MutableLiveData<String> getContentField() {
        return contentField;
    }

    public LiveData<EditPostResult.TitleError> getTitleFieldError() {
        return titleFieldError;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Boolean> getEditError() {
        return editError;
    }

    public LiveData<Boolean> getFinishEditPost() {
        return finishEditPost;
    }

    public LiveData<Boolean> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<String> getImageLink() {
        return imageLink;
    }
}

